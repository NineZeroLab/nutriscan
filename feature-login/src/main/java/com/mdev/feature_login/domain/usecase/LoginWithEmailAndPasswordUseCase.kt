package com.mdev.feature_login.domain.usecase

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AppUser>> = flow{
        when(val loginValidation = ValidateLoginCredentialsUseCase().invoke(email)){
            is Resource.Error -> {
                emit(Resource.Error(loginValidation.message.toString()))
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                emit(Resource.Loading())
                when(val loginResult = loginRepository.loginUserWithEmailAndPassword(email, password)){
                    is Resource.Error -> {
                        emit(Resource.Error(loginResult.message.toString()))
                    }
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                    is Resource.Success -> {
                        emit(Resource.Success(loginResult.data))
                    }
                }
            }
        }
    }
}