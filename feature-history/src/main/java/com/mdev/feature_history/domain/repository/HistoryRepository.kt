package com.mdev.feature_history.domain.repository

import com.mdev.client_firebase.data.remote.dto.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

internal interface HistoryRepository {
    suspend fun getSearchHistory(): Flow<List<SearchHistoryItem>>
}