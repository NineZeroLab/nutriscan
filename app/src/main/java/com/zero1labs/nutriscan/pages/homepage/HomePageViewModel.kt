package com.zero1labs.nutriscan.pages.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.models.data.AppUser
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.FirebaseCollection
import com.zero1labs.nutriscan.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomePageState(
    val product: Product? = null,
    val errorMsg: String? = null,
    val productScanState: ProductScanState = ProductScanState.NotStarted,
    val searchHistory: List<SearchHistoryListItem> = mutableListOf(),
    val firebaseDataFetchState: FirebaseDataFetchState = FirebaseDataFetchState.NotStarted,
    val appUser: AppUser? = null
)
enum class ProductScanState{
    Success,
    Failure,
    Loading,
    NotStarted,
}
enum class FirebaseDataFetchState{
    Loading,
    Success,
    Failure,
    NotStarted,
}
@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val appRepository: AppRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomePageState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let {user ->
                _uiState.update {state ->
                    state.copy(
                        appUser = AppUser(
                            name = user.email.toString(),
                            uid = user.uid
                        )
                    )
                }

            }
            Log.d(TAG,"Search History after viewModel init: ${uiState.value.searchHistory}")
        }
        fetchSearchHistory()
    }

    fun onEvent(event: HomePageEvent){
        when(event){
            is HomePageEvent.AddItemToHistory -> {

            }
            is HomePageEvent.FetchProductDetails -> {
                _uiState.update {
                    it.copy(
                        productScanState = ProductScanState.Loading
                    )
                }
                viewModelScope.launch {
                    val item: SearchHistoryListItem?
                    val response: Resource<Product> =
                        appRepository.getProductDetailsById(event.productId)
                    when(response){
                        is Resource.Success -> {

                            item = response.data?.let { product ->
                                SearchHistoryListItem(
                                    mainDetailsForView = MainDetailsForView.getMainDetailsForView(product),
                                    timeStamp = Timestamp.now()
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    productScanState = ProductScanState.Success,
                                    product = response.data,
                                )
                            }
                        }
                        is Resource.Error -> {
                            item = null
                           updateProductScanState(ProductScanState.Failure,response.message)
                        }
                    }
                   updateProductScanState(ProductScanState.NotStarted)
                    //adding a delay to prevent item being added to search history before the user is taken to product details page
                    addItemToSearchHistory(item)
                }

            }
            is HomePageEvent.SignOut -> {
                auth.signOut()
                _uiState.update {
                    it.copy(
                        appUser = null
                    )
                }
            }
            is HomePageEvent.UpdateUserDetails -> {
                updateUserDetails()
           }


        }
    }

    private fun addItemToFireStore(item: SearchHistoryListItem){
        auth.uid?.let {uid ->
            item?.mainDetailsForView?.productId?.let {id ->

                firestore.collection(FirebaseCollection.USERS)
                    .document(uid)
                    .collection(FirebaseCollection.SEARCH)
                    .document(id)
                    .set(item).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Log.d(TAG, "Document added successfully")
                        }else{
                            Log.d(TAG,task.exception.toString())
                        }
                    }
            }
        }
    }
    private fun addItemToSearchHistory(newItem: SearchHistoryListItem?){
        if (newItem == null){
            return
        }
        val searchHistory = _uiState.value.searchHistory.toMutableList()
        val updatedSearchHistory = searchHistory.filter { item ->
            item.mainDetailsForView.productId != newItem.mainDetailsForView.productId
        }.toMutableList().apply {
            add(0,newItem)
        }

        _uiState.update {state ->
            Log.d(TAG,"Adding ${newItem?.mainDetailsForView?.productName} to firebase")
            newItem?.let { addItemToFireStore(it) }
            state.copy(
                searchHistory = updatedSearchHistory
            )
        }

    }
    private fun updateUserDetails(){
        auth.currentUser?.let {user ->
            _uiState.update {state ->
                state.copy(
                    appUser = AppUser(
                        name = user.email.toString(),
                        uid = user.uid
                    ),
                )
            }
        }

    }
    private fun updateProductScanState(productScanState: ProductScanState, message: String? = null){
        _uiState.update {
            it.copy(
                productScanState = productScanState,
                errorMsg = message
            )
        }
    }
    private fun fetchSearchHistory(){
        if (_uiState.value.searchHistory.isNotEmpty()){
            return
        }
        _uiState.update {
            it.copy(
                firebaseDataFetchState = FirebaseDataFetchState.Loading
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.uid?.let {uid ->
                firestore.collection(FirebaseCollection.USERS)
                    .document(uid)
                    .collection(FirebaseCollection.SEARCH)
                    .get()
                    .addOnSuccessListener { documents ->
                        val searchHistoryListItems = mutableListOf<SearchHistoryListItem>()
                        for(doc in documents){
                            val item = doc.toObject(SearchHistoryListItem::class.java)
                            Log.d(TAG,"Successfully added ${item.mainDetailsForView.productName}")

                            searchHistoryListItems.add(element = item)
                        }
                        Log.d(TAG,"Successfully added $searchHistoryListItems")
                        _uiState.update {
                            it.copy(
                                searchHistory = searchHistoryListItems,
                                firebaseDataFetchState = FirebaseDataFetchState.Success
                            )
                        }
                    }
            }
        }
        _uiState.update {
            it.copy(
                firebaseDataFetchState = FirebaseDataFetchState.NotStarted
            )
        }
    }
}