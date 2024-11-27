package com.mdev.feature_analytics.presentation.analytics_page.domain.model

import com.mdev.openfoodfacts_client.domain.model.NutrientType

internal data class AnalyticsForView(
    val totalScans: Int,
    val goodScans: Int,
    val badScans: Int,
    val topCategories: List<ProductCategoryDetails>,
    val nutrientAnalytics: List<NutrientAnalyticsDetails>,
)

internal data class ProductCategoryDetails(
    val category: String,
    val count: Int
)

internal data class NutrientAnalyticsDetails(
    val nutrientType: NutrientType,
    val averageNutrientValue: Double,
    val unit: String,
)

