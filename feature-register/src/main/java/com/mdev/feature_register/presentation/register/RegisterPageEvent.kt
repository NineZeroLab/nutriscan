package com.mdev.feature_register.presentation.register

sealed class RegisterPageEvent {
    data class RegisterWithEmailAndPassword(
        val email: String,
        val password: String,
        val confirmPassword: String
    ): RegisterPageEvent()
}