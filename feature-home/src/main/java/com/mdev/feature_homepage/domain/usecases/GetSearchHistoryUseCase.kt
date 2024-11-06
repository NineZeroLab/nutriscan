package com.mdev.feature_homepage.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.domain.model.toSearchHistoryItem
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    operator fun invoke(): Flow<Resource<List<SearchHistoryItem>>> = flow{
        emit(Resource.Loading())
        try {
            homePageRepository.getSearchHistory().collect{ productDetails ->
                logger("found ${productDetails.size} items in search history")
                val searchHistory = productDetails.map { it.toSearchHistoryItem() }
                emit(Resource.Success(searchHistory))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}