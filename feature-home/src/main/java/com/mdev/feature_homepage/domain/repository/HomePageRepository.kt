package com.mdev.feature_homepage.domain.repository

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import kotlinx.coroutines.flow.StateFlow

interface HomePageRepository {
    suspend fun getSearchHistory(): StateFlow<List<ProductDetailsDto>>
    suspend fun getUserDetails(): AppUser?
    suspend fun logOut()
}