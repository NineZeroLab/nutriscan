package com.mdev.feature_register.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.feature_register.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterWithEmailAndPasswordUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<Resource<Unit>> = flow {
        val validateRegisterCredentialsUseCase = ValidateRegisterCredentialsUseCase()
        val validationResult = validateRegisterCredentialsUseCase(email, password, confirmPassword)
        if (validationResult is Resource.Error){
            emit(Resource.Error(validationResult.message.toString()))
        }
        if (validationResult is Resource.Success){
            emit(Resource.Loading())
            try {
                registerRepository.registerUserWithEmailAndPassword(email, password)
                emit(Resource.Success(Unit))
            }catch (e: Exception){
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}




