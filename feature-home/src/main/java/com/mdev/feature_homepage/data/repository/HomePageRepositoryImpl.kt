package com.mdev.feature_homepage.data.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

internal class HomePageRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val productRepository: ProductRepository
): HomePageRepository {
    override suspend fun getSearchHistory(): StateFlow<List<ProductDetailsDto>>  {
        return firebaseRepository.getSearchHistory()

    }

    override suspend fun getUserDetails(): AppUser? {
        return firebaseRepository.getCurrentUserDetails()
    }

    override suspend fun logOut() {
        firebaseRepository.signOutUser()
    }
}