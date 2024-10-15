package com.mdev.feature_history.data.model

import com.google.firebase.Timestamp
import com.mdev.client_firebase.data.remote.dto.MainDetailsForView
import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto

internal data class SearchHistoryItemDto(
    val mainDetailsForView: MainDetailsForView = MainDetailsForView(),
    val timestamp: Timestamp = Timestamp.now()
)



internal fun ProductDetailsDto.toSearchHistoryDto(): SearchHistoryItemDto{
    return SearchHistoryItemDto(
        mainDetailsForView, timestamp
    )
}