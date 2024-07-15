package com.zero1labs.nutriscan.models.data


data class AppUser(
    val name: String,
    val dietaryPreferences: List<NutrientPreference> = mutableListOf()
)
