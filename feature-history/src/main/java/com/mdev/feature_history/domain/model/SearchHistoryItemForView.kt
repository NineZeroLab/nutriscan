package com.mdev.feature_history.domain.model

import com.mdev.client_firebase.data.remote.dto.SearchHistoryItem
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import java.time.LocalDateTime
import java.time.ZoneId

internal data class SearchHistoryItemForView(
    val productId: String,
    val productName: String,
    val productBrand: String,
    val imageUrl: String,
    val healthCategory: HealthCategory,
    val timeStamp: LocalDateTime,
)


internal fun SearchHistoryItem.toSearchHistoryItemForView(): SearchHistoryItemForView {
    return SearchHistoryItemForView(
        productId = this.id,
        productBrand = this.brand,
        productName = this.name,
        imageUrl = this.imageUrl,
        healthCategory = getHealthCategory(this.nutriScoreGrade),
        timeStamp = LocalDateTime.ofInstant(this.timestamp.toInstant(), ZoneId.systemDefault())
    )
}


//Move to Utils
private fun getHealthCategory(nutriScoreGrade: String?) : HealthCategory {
    return when(nutriScoreGrade){
        "a" -> HealthCategory.HEALTHY
        "b" -> HealthCategory.GOOD
        "c" -> HealthCategory.FAIR
        "d" -> HealthCategory.POOR
        "e" -> HealthCategory.BAD
        else -> HealthCategory.UNKNOWN
    }
}
