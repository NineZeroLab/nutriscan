package com.mdev.feature_register.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.core.utils.isValidEmail
import com.mdev.core.utils.isValidPassword

class ValidateRegisterCredentialsUseCase {
    operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): Resource<Unit> {
        val emailValidity = email.isValidEmail()
        val passwordValidity = password.isValidPassword()
        if (!emailValidity.first){
            return Resource.Error(emailValidity.second)
        }
        if (!passwordValidity.first){
            return Resource.Error(passwordValidity.second)
        }
        if (password != confirmPassword){
            return Resource.Error("Passwords Don't match")
        }
        return Resource.Success(Unit)
    }
}