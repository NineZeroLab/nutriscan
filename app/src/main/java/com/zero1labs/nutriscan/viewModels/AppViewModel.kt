package com.zero1labs.nutriscan.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.models.data.AppUser
import com.zero1labs.nutriscan.models.data.NutrientPreference
import com.zero1labs.nutriscan.repository.AppRepository
import com.zero1labs.nutriscan.utils.NetworkUtils
import com.zero1labs.nutriscan.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import com.zero1labs.nutriscan.utils.AppResources.TAG
import kotlinx.coroutines.Dispatchers

data class ProductDetailsState(
    val product: Product? = null,
    val error: String? = null,
    val productScanState: ProductScanState = ProductScanState.NotStarted,
    val searchHistory : List<SearchHistoryListItem>  = mutableListOf(),
    val appUser: AppUser? = null,
)

enum class ProductScanState{
    Success,
    Failure,
    Loading,
    NotStarted,
}

@HiltViewModel
class AppViewModel @Inject constructor (
    private val appRepository: AppRepository,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsState())
    val uiState : StateFlow<ProductDetailsState> = _uiState.asStateFlow()

    fun onEvent(event : AppEvent) {
        when (event) {
            is AppEvent.GetProductDetails -> TODO()
            is AppEvent.OnStartScan -> {
//                checkIfOnline()
//                if (uiState.value.internetConnectionState != InternetConnectionState.Online) return
                viewModelScope.launch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            productScanState = ProductScanState.Loading
                        )
                    }
                    val response: Resource<Product> =
                        appRepository.getProductDetailsById(event.productId)
                    when (response) {
                        is Resource.Success -> {
                            _uiState.update { currentState ->
                                Log.d(TAG,"product scan success from viewmodel")
                                val searchHistoryListItem = response.data?.let { product ->
                                    SearchHistoryListItem(
                                        mainDetailsForView = MainDetailsForView.getMainDetailsForView(product),
                                        timeStamp = LocalDateTime.now()
                                    )
                                }
                                currentState.copy(
                                    productScanState = ProductScanState.Success,
                                    product = response.data,
                                    searchHistory = currentState.searchHistory.toMutableList().apply {
                                        if (searchHistoryListItem != null) {
                                            add(element = searchHistoryListItem, index = 0)
                                        }
                                    }
                                )
                            }
                        }
                        is Resource.Error -> _uiState.update { currentState ->
                            Log.d(TAG,"product scan failure from viewmodel")
                            currentState.copy(
                                productScanState = ProductScanState.Failure,
                                error = response.message,
                            )
                        }
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            productScanState = ProductScanState.NotStarted,
                        )
                    }

                }
            }
            is AppEvent.AddItemToHistory -> {
                Log.d(TAG, "Inside Firebase Event")
                val city = hashMapOf(
                    "name" to "Los Angeles",
                    "state" to "CA",
                    "country" to "USA",
                )


                firestore.collection("cities").document("LA")
                    .set(city)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }

            is AppEvent.SaveUserData -> {
                Log.d(TAG, "Saving user data in viewModel")
                _uiState.update { state ->
                    state.copy(
                        appUser = state.appUser?.copy(
                            name = event.userName,
                            dietaryPreferences = event.dietaryPreferences
                        )
                    )
                }
                //TODO: update user details in firebase as well
            }

            is AppEvent.SignInWithToken -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(event.idToken, null)
                viewModelScope.launch(Dispatchers.IO) {
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                Log.d(TAG, "Sign In with Firebase Credential Success")

                                _uiState.update { state ->
                                    state.copy(
                                        appUser = auth.currentUser?.displayName?.let {
                                            AppUser(
                                                name = it
                                            )
                                        }
                                    )
                                }
                            }else{
                                Log.d(TAG, "Sign In with Firebase Credential Failure")
                            }
                        }
                }
            }

            AppEvent.SignOut -> {
                auth.signOut()
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            appUser = null
                        )
                    }
                    Log.d(TAG, "User Signed Out")
                }
            }

            is AppEvent.SignInWithEmailAndPassword -> {
                auth.signInWithEmailAndPassword(event.email,event.password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d(TAG, "Successfully signed in using email and password")
                        viewModelScope.launch {
                            _uiState.update { state ->
                                state.copy(
                                    appUser = AppUser(
                                        name = auth.currentUser?.displayName ?: event.email,
                                    )
                                )
                            }
                        }
                    }
                }
            }

            is AppEvent.RegisterUserWithEmailAndPassword -> {
                auth.createUserWithEmailAndPassword(event.email, event.password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user = auth.currentUser
                        Log.d(TAG,"Successfully registered user with email and password!")
                        user?.sendEmailVerification()?.addOnCompleteListener {task ->
                            if (task.isSuccessful){

                            }
                        }

                    }else{
                        Log.d(TAG, "Error: ${task.exception}")
                    }
                }
            }
        }
    }

//    private fun checkIfOnline(){
//
//        val isOnline: Boolean = networkUtils.isNetworkAvailable()
//        _uiState.update { currentState ->
//                 currentState.copy(
//                     internetConnectionState = if (isOnline){
//                         InternetConnectionState.Online
//                     }else{
//                         InternetConnectionState.Offline
//                     }
//                 )
//             }
//        if(uiState.value.internetConnectionState != InternetConnectionState.Online) {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    productScanState = ProductScanState.NotStarted,
//                    internetConnectionState = InternetConnectionState.Unchecked
//                )
//            }
//        }
//    }

}