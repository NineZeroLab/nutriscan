package com.mdev.client_firebase.data.remote.dto

import com.google.firebase.Timestamp
import com.mdev.openfoodfacts_client.domain.model.HealthCategory

data class ProductDetailsDto(
    val mainDetailsForView: MainDetailsForView,
    val timestamp: Timestamp,
)

data class MainDetailsForView(
    val healthCategory: HealthCategory = HealthCategory.UNKNOWN,
    val imageUrl: String? = null,
    val productBrand: String? = null,
    val productId: String = "",
    val productName: String = "",
)