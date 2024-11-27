package com.mdev.feature_homepage.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.domain.model.toSearchHistoryItem
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import com.mdev.openfoodfacts_client.domain.model.ProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<SearchHistoryItem>>> =
        homePageRepository.getSearchHistory().map { productDetailsList ->
            if (productDetailsList.size > 10){
                Resource.Success(productDetailsList.subList(0,10).map { it.toSearchHistoryItem() })
            }else{
                Resource.Success(
                    productDetailsList.map { it.toSearchHistoryItem() }
                )
            }

        }.flowOn(Dispatchers.IO)
            .catch {
                Resource.Error<List<SearchHistoryItem>>("Error fetching search history")
            }
            .onStart {
                Resource.Loading<List<SearchHistoryItem>>()
            }

//    = flow{
//        emit(Resource.Loading())
//        try {
//
//            homePageRepository.
//
//
//
//            homePageRepository.getSearchHistory().collect{ productDetails ->
//                logger("found ${productDetails.size} items in search history")
//                val searchHistory = productDetails.map { it.toSearchHistoryItem() }
//                if (searchHistory.size > 10) {
//                    emit(Resource.Success(searchHistory.subList(0, 10)))
//                    return@collect
//                }
//                emit(Resource.Success(searchHistory))
//            }
//        }catch (e: Exception){
//            emit(Resource.Error(e.message.toString()))
//        }
//    }
}