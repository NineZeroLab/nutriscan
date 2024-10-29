package com.mdev.feature_history.domain.usecases

import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.feature_history.domain.model.SearchHistoryItem
import com.mdev.feature_history.domain.model.toSearchHistoryItem
import com.mdev.feature_history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetSearchHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<Resource<List<SearchHistoryItem>>> = flow{
        emit(Resource.Loading())
        try {
            historyRepository.getSearchHistory().collect{ searchHistoryDto ->
                emit(Resource.Success(searchHistoryDto))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}