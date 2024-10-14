package com.mdev.feature_product_details.domain.model

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.NutrientPreference

internal data class UserConsideration(
    val dietaryPreferences: List<NutrientPreference>,
    val dietaryRestrictions: List<DietaryRestriction>,
    val allergens: List<Allergen>
)

internal fun AppUser.getUserConsideration(): UserConsideration{
    val dietaryPreferences = this.dietaryPreferences
    val dietaryRestrictions = this.dietaryRestrictions
    val allergens = this.allergens
    return UserConsideration(
        dietaryPreferences = dietaryPreferences,
        dietaryRestrictions = dietaryRestrictions,
        allergens = allergens
    )
}