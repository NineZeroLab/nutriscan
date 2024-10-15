package com.mdev.client_firebase.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.client_firebase.utils.FirebaseCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): FirebaseRepository {

    private val _searchHistory : MutableStateFlow<List<ProductDetailsDto>> = MutableStateFlow<List<ProductDetailsDto>>(
        emptyList())

    /**
     * fetches the current user details from firestore
     */
    override suspend fun getCurrentUserDetails(): AppUser? {
        val userSnapShot = firestore.collection(FirebaseCollection.USERS)
            .document(auth.currentUser?.uid.toString())
            .get()
            .await()
        return userSnapShot.toObject(AppUser::class.java)
    }

    override suspend fun registerUserByEmailAndPassword(email: String, password: String): AuthResult {
        return  auth.createUserWithEmailAndPassword(email, password).await()
    }

    /**
     * login a user using firebase auth with email and password
     */
    override suspend fun loginUserByEmailAndPassword(email: String, password: String): AuthResult {
        val result =  auth.signInWithEmailAndPassword(email, password)
            .await()
        updateSearchHistory()
        return result
    }

    /**
     * creates a new user document in firestore
     */
    override suspend fun createUserDetails(appUser: AppUser) {
        firestore.collection(FirebaseCollection.USERS)
            .document(appUser.uid)
            .set(
                appUser,
                SetOptions.merge()
            ).await()
    }

    override suspend fun signOutUser(){
        auth.signOut()
    }

    /**
     * return the search history of the currently logged in user
     */
    override suspend fun getSearchHistory(): StateFlow<List<ProductDetailsDto>> {
        return _searchHistory.asStateFlow()
    }

    /**
     * Add the given product to firebase search history
     */
    override suspend fun addItemToSearchHistory(product: ProductDetailsDto) {
        auth.currentUser?.uid?.let { uid ->
            firestore.collection(FirebaseCollection.USERS)
                .document(uid)
                .collection(FirebaseCollection.SEARCH)
                .document(product.mainDetailsForView.productId)
                .set(product)
                .await()
            _searchHistory.value = _searchHistory.value.toMutableList().apply {
                add(product)
            }
        }
    }

    /**
     * Checks if a user is logged in already and updates the search history if logged in.
     * Meant for usage while opening the app.
     */
    override suspend fun isUserLoggedIn(): Boolean {
        if (auth.currentUser != null){
            updateSearchHistory()
            return true
        }
        return false
    }

    private suspend fun updateSearchHistory(){
        auth.currentUser?.uid?.let { uid ->
            val documentSnapshots = firestore.collection(FirebaseCollection.USERS)
                .document(uid).collection(FirebaseCollection.SEARCH)
                .get()
                .await()
                .documents
            _searchHistory.value = documentSnapshots.mapNotNull { doc: DocumentSnapshot ->
                doc.toObject(ProductDetailsDto::class.java)
            }
        }
    }
}