package com.mdev.feature_register.data.repository

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_register.domain.repository.RegisterRepository
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val  firebaseRepository: FirebaseRepository
):RegisterRepository {
    override suspend fun registerUserWithEmailAndPassword(email: String, password: String) {
        firebaseRepository.registerUserByEmailAndPassword(email, password)
    }
}