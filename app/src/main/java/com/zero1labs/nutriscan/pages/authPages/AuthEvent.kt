package com.zero1labs.nutriscan.pages.authPages


sealed class AuthEvent{
    data class SignInWithEmailAndPassword(val email: String, val password: String): AuthEvent()
    data class RegisterUserWithEmailAndPassword(val email: String, val password: String): AuthEvent()
}
