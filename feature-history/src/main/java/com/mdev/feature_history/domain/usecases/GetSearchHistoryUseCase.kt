package com.mdev.feature_history.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.feature_history.domain.model.SearchHistoryItemForView
import com.mdev.feature_history.domain.model.toSearchHistoryItemForView
import com.mdev.feature_history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetSearchHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<Resource<List<SearchHistoryItemForView>>> = flow {
        emit(Resource.Loading())
        try {
            historyRepository.getSearchHistory().collect { item ->
                val searchHistoryItemForView = item.map { it.toSearchHistoryItemForView() }
                emit(Resource.Success(searchHistoryItemForView))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}