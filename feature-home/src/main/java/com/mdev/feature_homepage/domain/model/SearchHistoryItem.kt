package com.mdev.feature_homepage.domain.model

import com.mdev.core.utils.TimeCalculator
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.ProductDetails
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

data class SearchHistoryItem(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val healthCategory: HealthCategory,
    val scannedTime: String,
)

internal fun ProductDetails.toSearchHistoryItem(): SearchHistoryItem{
    val scannedTime = LocalTime.now().minusHours(3)
    val scannedTimeString = TimeCalculator.getTime(Duration.between(scannedTime,LocalDateTime.now()))
    return SearchHistoryItem(
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



fun getDummyHistoryitem (): List<SearchHistoryItem> {
    val searchHistoryItem = mutableListOf<SearchHistoryItem>()
    for (i in 1..10) {
        searchHistoryItem.add(
            SearchHistoryItem(
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



