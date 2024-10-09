package com.mdev.feature_login.presentation.loginPage

sealed class LoginEvent {
    data class LoginWithEmailAndPassword(
        val email: String,
        val password: String
    ): LoginEvent()
}