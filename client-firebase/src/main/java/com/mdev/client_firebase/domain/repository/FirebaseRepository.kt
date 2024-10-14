package com.mdev.client_firebase.domain.repository

import com.google.firebase.auth.AuthResult
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import kotlinx.coroutines.flow.StateFlow

interface FirebaseRepository {
    suspend fun getCurrentUserDetails(): AppUser?
    suspend fun registerUserByEmailAndPassword(email: String, password: String): AuthResult
    suspend fun loginUserByEmailAndPassword(email: String, password: String): AuthResult
    suspend fun createUserDetails(appUser: AppUser)
    suspend fun signOutUser()
    suspend fun getSearchHistory(): StateFlow<List<ProductDetailsDto>>
    suspend fun addItemToSearchHistory(product: ProductDetailsDto)
}