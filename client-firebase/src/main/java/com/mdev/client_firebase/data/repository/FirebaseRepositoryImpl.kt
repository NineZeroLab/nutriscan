package com.mdev.client_firebase.data.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mdev.client_firebase.data.remote.dto.AnalyticsData
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.data.remote.dto.calculateAnalytics
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.client_firebase.utils.FirebaseCollection
import com.mdev.common.utils.Resource
import com.mdev.openfoodfacts_client.domain.model.ProductDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): FirebaseRepository {

    private val _searchHistory : MutableStateFlow<List<ProductDetailsDto>> = MutableStateFlow(emptyList())

    private val _searchHistoryWithDetails: MutableStateFlow<List<ProductDetails>> = MutableStateFlow(emptyList())

    private val _analyticsData: MutableStateFlow<AnalyticsData> = MutableStateFlow(AnalyticsData())

    private var _userDetails: AppUser? = null

    /**
     * fetches the current user details from firestore
     */
    override suspend fun getCurrentUserDetails(): AppUser? {
        if (auth.currentUser == null){
            Log.i("logger", "getCurrentUserDetails: current user is null")
            return null
        }
        val userSnapShot = firestore.collection(FirebaseCollection.USERS)
            .document(auth.currentUser?.uid.toString())
            .get()
            .await()

        _userDetails = userSnapShot.toObject(AppUser::class.java)
        Log.i("logger", "getCurrentUserDetails: userId: ${auth.currentUser?.uid}")
        return userSnapShot.toObject(AppUser::class.java)
    }

    override suspend fun registerUserByEmailAndPassword(email: String, password: String): AuthResult {
        val authResult =  auth.createUserWithEmailAndPassword(email, password).await()
        authResult.user?.let {
            createUserDetails(
                AppUser(
                    name = it.email ?: "",
                    uid = it.uid
                )
            )
        }
        return authResult
    }

    /**
     * login a user using firebase auth with email and password
     */
    override suspend fun loginUserByEmailAndPassword(email: String, password: String): AuthResult {
        val result =  auth.signInWithEmailAndPassword(email, password)
            .await()
        getCurrentUserDetails()
        updateSearchHistory()
        updateSearchHistoryWithDetails()
        updateAnalytics()
        return result
    }

    /**
     * creates a new user document in firestore
     */
    override suspend fun createUserDetails(appUser: AppUser) {
        Log.i("logger", "createUserDetails: creating firestore doc for user ${appUser.uid}")
        firestore.collection(FirebaseCollection.USERS)
            .document(appUser.uid)
            .set(
                appUser,
                SetOptions.merge()
            ).await()
    }
    /**
     * return the search history of the currently logged in user
     */
    override suspend fun getSearchHistory(): StateFlow<List<ProductDetailsDto>> {
        return _searchHistory.asStateFlow()
    }

    override suspend fun getSearchHistoryWithDetails(): StateFlow<List<ProductDetails>>{
        return _searchHistoryWithDetails.asStateFlow()
    }

    override suspend fun getAnalyticsData(): StateFlow<AnalyticsData> {
        return _analyticsData.asStateFlow()
    }


    override suspend fun updateUserDetails(appUser: AppUser): Resource<Unit> {
        try {
            firestore.collection(FirebaseCollection.USERS)
                .document(appUser.uid)
                .set(
                    appUser,
                    SetOptions.merge()
                ).await()
            _userDetails = appUser
            return Resource.Success(Unit)
        }catch (e: Exception){
            return Resource.Error(e.message.toString())
        }

    }

    /**
     * Add the given product to firebase search history
     */
    @Deprecated(
        message = "sends only the basic details to firestore",
        replaceWith = ReplaceWith("addProductToSearchHistory()")
    )
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
            updateSearchHistoryWithDetails()
            getCurrentUserDetails()
            updateAnalytics()
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

    private suspend fun updateSearchHistoryWithDetails(){
        auth.currentUser?.uid?.let { uid ->
            val documentSnapshots = firestore.collection(FirebaseCollection.USERS)
                .document(uid).collection(FirebaseCollection.SEARCH)
                .get()
                .await()
                .documents
            _searchHistoryWithDetails.value = documentSnapshots.mapNotNull { doc: DocumentSnapshot ->
                doc.toObject(ProductDetails::class.java)
            }
            Log.d("logger", _searchHistoryWithDetails.value.toString())
            updateAnalytics()
        }
    }


    override suspend fun addProductToSearchHistory(productDetails: ProductDetails){
        auth.currentUser?.uid?.let { uid ->
            firestore.collection(FirebaseCollection.USERS)
                .document(uid)
                .collection(FirebaseCollection.SEARCH)
                .document(productDetails.id)
                .set(productDetails)
                .await()
            _searchHistoryWithDetails.value.let { searchHistory ->
                if (searchHistory.none(){ it.id == productDetails.id}){
                    _searchHistoryWithDetails.value = searchHistory.toMutableList().apply {
                        add(productDetails)
                    }
                }
            }
            updateAnalytics()
        }
    }

    private fun updateAnalytics() {
        if (_userDetails == null){
            Log.d("logger", "user details is null")
        }
        _userDetails?.let { _userDetails ->
            _analyticsData.value = _searchHistoryWithDetails.value.calculateAnalytics(_userDetails.name)
            Log.d("logger", _analyticsData.value.toString())
        }
    }

    override suspend fun logout() {
        auth.signOut()
        clearUserDetails()
    }

    private fun clearUserDetails() {
        _userDetails?.let {
            _analyticsData.value = AnalyticsData(it.name)
            _searchHistory.value = emptyList()
            _searchHistoryWithDetails.value = emptyList()
            }
        }
}