package com.zero1labs.nutriscan.domain.model

import com.mdev.core.domain.model.Allergen
import com.mdev.core.domain.model.DietaryRestriction
import com.mdev.core.domain.model.NutrientPreference


data class AppUser(
    val name: String = "",
    val uid: String = "",
    val profileUpdated: Boolean = false,
    val dietaryPreferences: List<NutrientPreference> = mutableListOf(),
    val dietaryRestrictions: List<DietaryRestriction> = mutableListOf(),
    val allergens: List<Allergen> = mutableListOf(),
)
