package com.mdev.feature_login.domain.usecase

import com.mdev.common.utils.Resource
import com.mdev.core.utils.isValidEmail

class ValidateLoginCredentialsUseCase{
    operator fun invoke(email: String): Resource<Unit>{
        val emailValidation = email.isValidEmail()
        if (!emailValidation.first){
            return Resource.Error(emailValidation.second)
        }
        return Resource.Success(Unit)
    }
}