package com.mdev.feature_login.presentation.loginPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.common.utils.Resource
import com.mdev.feature_login.domain.usecase.IsUserLoggedInUseCase
import com.mdev.feature_login.domain.usecase.LoginWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginPageState(
    val loginStatus: LoginStatus = LoginStatus.IDLE,
    val errorMessage: String? = null,
)

enum class LoginStatus{
    LOADING,
    SUCCESS,
    FAILURE,
    IDLE
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginPageState())
    val uiState = _uiState.asStateFlow()
    init {
        getCurrentUser()
    }

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.LoginWithEmailAndPassword -> loginWithEmailAndPassword(event.email, event.password)
            }
        }
    private fun getCurrentUser(){
        isUserLoggedInUseCase().onEach { result ->
            when(result){
                is Resource.Error -> {
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.FAILURE,
                            errorMessage = result.message
                        )
                    }
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    if (result.data == true){
                        _uiState.update {
                            LoginPageState(
                                loginStatus = LoginStatus.SUCCESS
                            )
                        }
                        _uiState.update {
                            LoginPageState(
                                loginStatus = LoginStatus.IDLE
                            )
                        }
                    }else{
                        _uiState.update {
                            LoginPageState(
                            loginStatus = LoginStatus.IDLE
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        loginWithEmailAndPasswordUseCase(email,password).onEach {result ->
            when(result){
                is Resource.Error -> {
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.FAILURE,
                            errorMessage = result.message
                        )
                    }
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.SUCCESS
                        )
                    }
                    _uiState.update {
                        LoginPageState(
                            loginStatus = LoginStatus.IDLE
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

    }
}