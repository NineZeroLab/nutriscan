package com.mdev.client_firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mdev.client_firebase.data.repository.FirebaseRepositoryImpl
import com.mdev.client_firebase.domain.repository.FirebaseRepository

class FirebaseClient {
    operator fun invoke(): FirebaseRepositoryImpl {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        return FirebaseRepositoryImpl(
            auth = auth,
            firestore = firestore
        )
    }
}