package com.mdev.feature_homepage.domain.model

import com.mdev.client_firebase.data.remote.dto.SearchHistoryItem
import com.mdev.core.utils.TimeCalculator
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

internal data class SearchHistoryItemForView(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val healthCategory: HealthCategory,
    val scannedTime: String,
)

internal fun SearchHistoryItem.toSearchHistoryItemForView(): SearchHistoryItemForView {
    val scannedTime = LocalTime.now().minusHours(3)
    val scannedTimeString = TimeCalculator.getTime(Duration.between(scannedTime,LocalDateTime.now()))
    return SearchHistoryItemForView(
        productId = this.id,
        productName = this.name,
        imageUrl = this.imageUrl,
        healthCategory = getHealthCategory(this.nutriScoreGrade),
        scannedTime = scannedTimeString
    )
}

internal fun getHealthCategory(nutriScoreGrade: String?) : HealthCategory {
    return when(nutriScoreGrade){
        "a" ,
        "b" -> HealthCategory.GOOD
        "c" -> HealthCategory.FAIR
        "d" ,
        "e" -> HealthCategory.BAD
        else -> HealthCategory.UNKNOWN
    }
}


internal fun getDummyHistoryItem(): List<SearchHistoryItemForView> {
    val searchHistoryItem = mutableListOf<SearchHistoryItemForView>()
    for (i in 1..10) {
        searchHistoryItem.add(
            SearchHistoryItemForView(
                productId = "12312421",
                productName = "Test",
                imageUrl = "https://images.openfoodfacts.org/images/products/301/762/042/2003/front_en.633.400.jpg",
                healthCategory = HealthCategory.BAD,
                scannedTime = "2 Days ago",
            )
        )
    }
    return searchHistoryItem
}



