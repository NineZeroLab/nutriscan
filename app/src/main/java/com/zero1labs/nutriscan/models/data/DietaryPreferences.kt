package com.zero1labs.nutriscan.models.data

import com.zero1labs.nutriscan.data.models.Nutrient
import com.zero1labs.nutriscan.utils.NutrientType


data class NutrientPreference(
    val nutrientType: NutrientType? = null,
    val nutrientPreferenceType: NutrientPreferenceType? = null
)

enum class NutrientPreferenceType{
    LOW,
    MODERATE,
    HIGH
}
