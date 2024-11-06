package com.mdev.feature_homepage.utils

import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.common.R as commonRes

fun HealthCategory.getIcon():Int {
    return when(this){
        HealthCategory.HEALTHY,
        HealthCategory.GOOD -> commonRes.drawable.circle_good
        HealthCategory.FAIR -> commonRes.drawable.circle_moderate
        HealthCategory.POOR,
        HealthCategory.BAD -> commonRes.drawable.circle_bad
        HealthCategory.UNKNOWN -> commonRes.drawable.circle_unknown
    }
}