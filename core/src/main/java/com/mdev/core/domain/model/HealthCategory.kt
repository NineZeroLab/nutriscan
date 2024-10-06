package com.mdev.core.domain.model

enum class HealthCategory(val description: String){
    HEALTHY("Healthy"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor"),
    BAD("Bad"),
    UNKNOWN("Unknown")
}