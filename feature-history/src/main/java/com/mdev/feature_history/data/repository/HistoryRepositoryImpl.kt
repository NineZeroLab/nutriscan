package com.mdev.feature_history.data.repository

import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_history.data.model.SearchHistoryItemDto
import com.mdev.feature_history.data.model.toSearchHistoryDto
import com.mdev.feature_history.domain.model.SearchHistoryItem
import com.mdev.feature_history.domain.model.toSearchHistoryItem
import com.mdev.feature_history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HistoryRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): HistoryRepository {
    override suspend fun getSearchHistory(): Flow<List<SearchHistoryItem>> {
        return firebaseRepository.getSearchHistoryWithDetails().map { searchHistoryList ->
            searchHistoryList.map { productDetailsDto ->
                productDetailsDto.toSearchHistoryItem()
            }
        }
    }
}