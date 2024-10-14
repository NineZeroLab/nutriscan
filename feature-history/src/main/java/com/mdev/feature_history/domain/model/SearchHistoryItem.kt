package com.mdev.feature_history.domain.model

import com.mdev.feature_history.data.model.SearchHistoryItemDto
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import java.time.LocalDateTime
import java.time.ZoneId

internal data class SearchHistoryItem(
    val productId: String,
    val productName: String,
    val productBrand: String,
    val imageUrl: String,
    val healthCategory: HealthCategory,
    val timeStamp: LocalDateTime,
)



internal fun SearchHistoryItemDto.toSearchHistoryItem(): SearchHistoryItem{
    return SearchHistoryItem(
        productId = this.mainDetailsForView.productId,
        productName = this.mainDetailsForView.productName,
        imageUrl = this.mainDetailsForView.imageUrl ?: "",
        productBrand = this.mainDetailsForView.productBrand ?: "",
        healthCategory = this.mainDetailsForView.healthCategory,
        timeStamp = LocalDateTime.ofInstant(this.timestamp.toInstant(), ZoneId.systemDefault())
    )
}