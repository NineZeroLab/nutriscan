package com.zero1labs.nutriscan.domain.model

import com.zero1labs.nutriscan.utils.Allergen
import com.zero1labs.nutriscan.utils.DietaryRestriction


data class AppUser(
    val name: String = "",
    val uid: String = "",
    val profileUpdated: Boolean = false,
    val dietaryPreferences: List<NutrientPreference> = mutableListOf(),
    val dietaryRestrictions: List<DietaryRestriction> = mutableListOf(),
    val allergens: List<Allergen> = mutableListOf(),
)
