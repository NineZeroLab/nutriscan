package com.mdev.openfoodfacts_client.domain.model

enum class HealthCategory(val description: String){
    HEALTHY("Healthy"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor"),
    BAD("Bad"),
    UNKNOWN("Unknown")
}