package com.mdev.openfoodfacts_client.domain.model

import android.util.Log
import com.mdev.openfoodfacts_client.utils.ClientResources.TAG


data class NutrientPreference(
    val nutrientType: NutrientType? = null,
    val nutrientPreferenceType: NutrientPreferenceType? = null
)

enum class NutrientPreferenceType{
    LOW,
    MODERATE,
    HIGH,
    UNKNOWN
}

fun getDietaryPreferenceConclusion(productPreferences: List<NutrientPreference>, userPreferences: List<NutrientPreference>?): String {
    Log.d(TAG,"user Preferences: $userPreferences")
    //filtering user preference to remove null values in nutrientPreference type stored in firebase
    val filteredUserPreference = userPreferences?.filter { nutrientPreference ->
        nutrientPreference.nutrientPreferenceType != null
    }
    if (productPreferences.isEmpty()){
        return "Not enough data available to calculate Dietary Preferences."
    }
    if (filteredUserPreference.isNullOrEmpty()){
        return ""
    }
    val commonPreferences = mutableListOf<NutrientPreference>()
    productPreferences.forEach { productPreference ->
        filteredUserPreference.forEach { nutrientPreference ->
            if (productPreference == nutrientPreference){
                commonPreferences.add(nutrientPreference)
            }
        }
    }
    if (commonPreferences.isEmpty()){
        return "None of the nutrients match your preference"
    }
    if (commonPreferences.size == filteredUserPreference.size){
        return "All the nutrients match your preference"
    }
    return "Some of the nutrients match your preference"
}
