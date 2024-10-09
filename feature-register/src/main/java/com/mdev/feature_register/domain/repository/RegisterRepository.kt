package com.mdev.feature_register.domain.repository

interface RegisterRepository {
    suspend fun registerUserWithEmailAndPassword(email: String, password: String)
}