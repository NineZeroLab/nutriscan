package com.mdev.feature_register.presentation.register

import com.mdev.common.utils.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.feature_register.domain.usecases.RegisterWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class RegisterPageState(
    val errorMessage: String? = null,
    val registerStatus: RegisterStatus = RegisterStatus.IDLE
) {
}

enum class RegisterStatus{
    LOADING,
    SUCCESS,
    FAILURE,
    IDLE
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterPageState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: RegisterPageEvent){
        when(event){
            is RegisterPageEvent.RegisterWithEmailAndPassword -> registerUserWithEmailAndPassword(
                event.email,
                event.password,
                event.confirmPassword
            )
    }
}

    private fun registerUserWithEmailAndPassword(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        registerWithEmailAndPasswordUseCase(
            email, password, confirmPassword
        ).onEach { result: Resource<Unit> ->
            when(result){
                is Resource.Loading -> {
                    _uiState.update {
                        RegisterPageState(
                            registerStatus = RegisterStatus.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        RegisterPageState(
                            registerStatus = RegisterStatus.SUCCESS
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        RegisterPageState(
                            registerStatus = RegisterStatus.FAILURE,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}
