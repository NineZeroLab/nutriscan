package com.zero1labs.nutriscan.presentation.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zero1labs.nutriscan.domain.model.MainDetailsForView
import com.zero1labs.nutriscan.domain.model.SearchHistoryListItem
import com.zero1labs.nutriscan.data.remote.dto.ProductDto
import com.zero1labs.nutriscan.domain.model.AppUser
import com.zero1labs.nutriscan.data.repository.ProductRepositoryImpl
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.FirebaseCollection
import com.zero1labs.nutriscan.utils.Resource
import com.zero1labs.nutriscan.utils.logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class HomePageState(
    val product: ProductDto? = null,
    val msg: String? = null,
    val productScanState: ProductScanState = ProductScanState.NotStarted,
    val searchHistory: List<SearchHistoryListItem> = mutableListOf(),
    val firebaseDataFetchState: FirebaseDataFetchState = FirebaseDataFetchState.Loading,
    val appUser: AppUser? = null,
    val userDetailsUpdateState: UserDetailsUpdateState = UserDetailsUpdateState.NOT_STARTED
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
enum class UserDetailsUpdateState{
    LOADING,
    SUCCESS,
    FAILURE,
    NOT_STARTED
}
@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val appRepository: ProductRepositoryImpl,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomePageState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                firebaseDataFetchState = FirebaseDataFetchState.Loading
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            auth.currentUser?.let {user ->
                withContext(Dispatchers.Main){
                    _uiState.update {state ->
                        state.copy(
                            appUser = AppUser(
                                name = user.email.toString(),
                                uid = user.uid,

                                ),
                            firebaseDataFetchState = FirebaseDataFetchState.Success
                        )
                    }
                    _uiState.update {
                        it.copy(
                            firebaseDataFetchState = FirebaseDataFetchState.NotStarted
                        )
                    }
                }
                _uiState.update {
                    it.copy(
                        firebaseDataFetchState = FirebaseDataFetchState.NotStarted
                    )
                }

            }
            Log.d(TAG,"Search History after viewModel init: ${uiState.value.searchHistory}")
        }
    }

    fun onEvent(event: HomePageEvent){
        when(event){
            is HomePageEvent.AddItemToHistory -> {

            }
            is HomePageEvent.FetchProductDetails -> {
                fetchProductDetails(event.productId)

            }
            is HomePageEvent.SignOut -> {
                auth.signOut()
                _uiState.update {
                    HomePageState().copy(
                        firebaseDataFetchState = FirebaseDataFetchState.NotStarted
                    )
                }
            }
            is HomePageEvent.UpdateUserDetails -> {
                updateUserDetails()
                fetchSearchHistory()
           }

            is HomePageEvent.UpdateUserPreferences -> {
                updateUserPreferences(event.appUser)
                updateUserDetails()
            }
        }
    }

    private fun fetchProductDetails(productId: String) {
        _uiState.update {
            it.copy(
                productScanState = ProductScanState.Loading
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.getProductDetailsById(productId){response ->
                when (response) {
                    is Resource.Success -> {

                        val item = response.data?.let { product ->
                            SearchHistoryListItem(
                                mainDetailsForView = MainDetailsForView.getMainDetailsForView(product),
                                timeStamp = Timestamp.now()
                            )
                        }

                        viewModelScope.launch{
                            updateState(
                                productScanState = ProductScanState.Success,
                                product = response.data
                            )
                            updateProductScanState(ProductScanState.NotStarted)
                            addItemToSearchHistory(item)
                        }

                    }
                    is Resource.Error -> {
                        logger("Error in viewModel while fetching product details")
                        viewModelScope.launch {
                            updateProductScanState(ProductScanState.Failure, response.message)
                            updateProductScanState(ProductScanState.NotStarted)
                        }
                    }
                }
            }
        }
    }

    private fun updateUserPreferences(appUser: AppUser) {
        if (appUser.uid == ""){
            return
        }
        _uiState.update {
            it.copy(
                userDetailsUpdateState = UserDetailsUpdateState.LOADING
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection(FirebaseCollection.USERS)
                .document(appUser.uid)
                .set(appUser, SetOptions.merge())
                .addOnSuccessListener {
                    _uiState.update {
                        it.copy(
                            userDetailsUpdateState = UserDetailsUpdateState.SUCCESS,
                            appUser = appUser,
                            msg = AppResources.USER_DETAILS_UPDATED,

                        )

                    }
                    _uiState.update {
                        it.copy(
                            userDetailsUpdateState = UserDetailsUpdateState.NOT_STARTED
                        )
                    }
                }.addOnFailureListener { exception ->
                    _uiState.update {
                        it.copy(
                            userDetailsUpdateState = UserDetailsUpdateState.FAILURE,
                            msg = "${AppResources.USER_DETAILS_UPDATE_FAILURE} ${exception.message}"
                        )
                    }
                    _uiState.update {
                        it.copy(
                            userDetailsUpdateState = UserDetailsUpdateState.NOT_STARTED
                        )
                    }
                }

        }

    }

    private fun addItemToFireStore(item: SearchHistoryListItem){
        auth.uid?.let {uid ->
            item.mainDetailsForView.productId?.let {id ->

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
            Log.d(TAG,"Adding ${newItem.mainDetailsForView.productName} to firebase")
            addItemToFireStore(newItem)
            state.copy(
                searchHistory = updatedSearchHistory
            )
        }

    }
    private fun updateUserDetails(){
//        auth.currentUser?.let {user ->
//            _uiState.update {state ->
//                state.copy(
//                    appUser = AppUser(
//                        name = user.email.toString(),
//                        uid = user.uid
//                    ),
//                )
//            }
//        }
        val id = auth.currentUser?.uid.toString()
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    firebaseDataFetchState = FirebaseDataFetchState.Loading
                )
            }
            firestore.collection(FirebaseCollection.USERS)
                .document(id)
                .get()
                .addOnSuccessListener { userSnapshot ->
                   _uiState.update {
                       it.copy(
                           appUser = userSnapshot.toObject(AppUser::class.java),
                           firebaseDataFetchState = FirebaseDataFetchState.Success
//                           appUser = AppUser()
                       )
                   }
                    _uiState.update {
                        it.copy(
                            firebaseDataFetchState = FirebaseDataFetchState.NotStarted
                        )
                    }
                }


        }
        Log.d(TAG, "Profile detailed updated from firebase ${uiState.value.appUser?.allergens}")



    }
    private fun updateProductScanState(productScanState: ProductScanState, message: String? = null){
        _uiState.update {
            it.copy(
                productScanState = productScanState,
                msg = message
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
                        _uiState.update {
                            it.copy(
                                firebaseDataFetchState = FirebaseDataFetchState.NotStarted
                            )
                        }
                    }.addOnFailureListener {
                        Log.d(TAG, "Search History Fetch Failure")
                    }

            }
        }

    }

//    data class HomePageState(
//        val product: Product? = null,
//        val msg: String? = null,
//        val productScanState: ProductScanState = ProductScanState.NotStarted,
//        val searchHistory: List<SearchHistoryListItem> = mutableListOf(),
//        val firebaseDataFetchState: FirebaseDataFetchState = FirebaseDataFetchState.Loading,
//        val appUser: AppUser? = null,
//        val userDetailsUpdateState: UserDetailsUpdateState = UserDetailsUpdateState.NOT_STARTED
//    )
    private fun updateState(
    product: ProductDto? = _uiState.value.product,
    msg: String? = _uiState.value.msg,
    productScanState: ProductScanState = _uiState.value.productScanState,
    searchHistory: List<SearchHistoryListItem> = _uiState.value.searchHistory,
    firebaseDataFetchState: FirebaseDataFetchState = _uiState.value.firebaseDataFetchState,
    appUser: AppUser? = _uiState.value.appUser,
    userDetailsUpdateState: UserDetailsUpdateState = _uiState.value.userDetailsUpdateState,
    ){

        _uiState.update {
            it.copy(
                product = product,
                msg = msg,
                productScanState = productScanState,
                searchHistory = searchHistory,
                firebaseDataFetchState = firebaseDataFetchState,
                appUser = appUser,
                userDetailsUpdateState = userDetailsUpdateState,
            )
        }
    }
}