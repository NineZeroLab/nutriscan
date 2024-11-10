package com.mdev.feature_homepage.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_homepage.domain.model.SearchHistoryItemForView
import com.mdev.feature_homepage.domain.model.toSearchHistoryItemForView
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetSearchHistoryUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    operator fun invoke(): Flow<Resource<List<SearchHistoryItemForView>>> = flow {
        emit(Resource.Loading())
        try {
            homePageRepository.getSearchHistory().collect { searchHistoryItem ->
                logger("found ${searchHistoryItem.size} items in search history")
                val searchHistory = searchHistoryItem.map { it.toSearchHistoryItemForView() }
                emit(Resource.Success(searchHistory))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}