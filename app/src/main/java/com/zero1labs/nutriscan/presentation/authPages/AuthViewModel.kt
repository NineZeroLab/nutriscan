package com.zero1labs.nutriscan.presentation.authPages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.repository.FirebaseRepositoryImpl
import com.mdev.core.utils.AppResources.TAG
import com.mdev.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val signInStatus: SignInStatus = SignInStatus.SIGNED_OUT,
    val registerStatus: RegisterStatus = RegisterStatus.NOT_STARTED,
    val errorMsg: String? = null,
    val appUser: AppUser? =null,
)

enum class SignInStatus{
    LOADING,
    SIGNED_IN,
    SIGNED_OUT,
    ERROR,
    NOT_STARTED
}
enum class RegisterStatus{
    LOADING,
    SUCCESS,
    FAILURE,
    NOT_STARTED
}


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseClient: FirebaseRepositoryImpl
): ViewModel(){
    private val _uiState = MutableStateFlow<AuthState>(AuthState())
    val uiState = _uiState.asStateFlow()

    init {
//        auth.currentUser?.let {
//            _uiState.update { state ->
//                state.copy(
//                    signInStatus = SignInStatus.SIGNED_IN
//                )
//            }
//        }
    }

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.SignInWithEmailAndPassword ->{
                viewModelScope.launch {
                    firebaseClient.signInUserByEmailAndPassword(
                        event.email, event.password
                    ).onEach { resource ->
                        when(resource){
                            is Resource.Error -> {
                                _uiState.update {
                                    AuthState(
                                        errorMsg = resource.message,
                                        signInStatus = SignInStatus.ERROR
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                _uiState.update {
                                    AuthState(
                                        signInStatus = SignInStatus.LOADING
                                    )
                                }
                            }
                            is Resource.Success -> {
                                _uiState.update {
                                    AuthState(
                                        signInStatus = SignInStatus.SIGNED_IN
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AuthEvent.RegisterUserWithEmailAndPassword -> {
                viewModelScope.launch {
                    firebaseClient.registerUserByEmailAndPassword(
                        event.email, event.password
                    ).onEach { resource ->
                        when(resource){
                            is Resource.Error -> {
                                _uiState.update {
                                    AuthState(
                                        errorMsg = resource.message,
                                        registerStatus = RegisterStatus.FAILURE
                                    )
                                }
                            }
                            is Resource.Loading -> {
                                _uiState.update {
                                    AuthState(
                                        registerStatus = RegisterStatus.LOADING
                                    )
                                }
                            }
                            is Resource.Success -> {
                                _uiState.update {
                                    AuthState(
                                        registerStatus = RegisterStatus.SUCCESS
                                    )
                                }
                                _uiState.update {
                                    AuthState(
                                        registerStatus = RegisterStatus.NOT_STARTED
                                    )
                                }
                            }
                        }
                    }
                }
           }
        }
    }

    private fun fetchUserDetails() {

        viewModelScope.launch {
            firebaseClient.getUserDetailsById().onEach { resource ->
                when(resource){
                    is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    errorMsg = resource.message
                                )
                            }
                    }
                    is Resource.Loading -> {
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.LOADING
                                )
                            }
                    }
                    is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.SIGNED_IN
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.NOT_STARTED
                                )
                            }
                        }
                    }
                }
            }
        }

    //TODO: This should be moved to use chase which gets called on new user registration
    private fun createFirebaseUser(firebaseUser: FirebaseUser){
        viewModelScope.launch {
            _uiState.value.appUser?.let { firebaseClient.createUserDetails(appUser = it) }
        }
    }
}