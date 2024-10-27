package com.mdev.feature_history.domain.repository

import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.feature_history.data.model.SearchHistoryItemDto
import com.mdev.feature_history.domain.model.SearchHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface HistoryRepository {
    suspend fun getSearchHistory(): Flow<List<SearchHistoryItem>>
}