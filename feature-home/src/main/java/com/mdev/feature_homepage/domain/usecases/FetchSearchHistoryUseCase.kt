package com.mdev.feature_homepage.domain.usecases

import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FetchSearchHistoryUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    suspend operator fun invoke(): StateFlow<List<SearchHistoryListItem>> {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        return homePageRepository.getSearchHistory().map { productDetailsDtoList ->
            productDetailsDtoList.map {  productDto ->
                SearchHistoryListItem(
                    mainDetailsForView = productDto.mainDetailsForView,
                    timeStamp = productDto.timestamp
                )
            }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }
}