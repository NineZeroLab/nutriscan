package com.mdev.feature_history.data.repository

import com.mdev.client_firebase.data.remote.dto.SearchHistoryItem
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.feature_history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class HistoryRepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): HistoryRepository {
    override suspend fun getSearchHistory(): Flow<List<SearchHistoryItem>> {
        return firebaseRepository.getSearchHistoryWithDetails()
    }
}