package com.mdev.openfoodfacts_client.domain.model

enum class NutrientCategory(val header : String){
    POSITIVE ("Positives"),
    NEGATIVE ("Negatives"),
    UNKNOWN ("Unknown")
}