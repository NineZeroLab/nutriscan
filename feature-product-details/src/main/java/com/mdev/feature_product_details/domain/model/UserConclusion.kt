package com.mdev.feature_product_details.domain.model

import com.mdev.openfoodfacts_client.domain.model.getAllergenConclusion
import com.mdev.openfoodfacts_client.domain.model.getDietaryPreferenceConclusion
import com.mdev.openfoodfacts_client.domain.model.getDietaryRestrictionConclusion


//Needs to be renamed!!!
internal data class UserConclusion(
    val dietaryPreferenceConclusion: String,
    val dietaryRestrictionConclusion: String,
    val allergenConclusion: String
)

internal fun Considerations.getConclusion(productConsiderations: Considerations): UserConclusion{
    val dietaryPreferenceConclusion = getDietaryPreferenceConclusion(
        productPreferences = productConsiderations.dietaryPreferences,
        userPreferences = this.dietaryPreferences
    )
    val dietaryRestrictionConclusion = getDietaryRestrictionConclusion(
        productRestrictions = productConsiderations.dietaryRestrictions,
        userRestrictions = this.dietaryRestrictions
    )
    val allergenConclusion = getAllergenConclusion(
        productAllergens = productConsiderations.allergens,
        userAllergens = this.allergens
    )
    return UserConclusion(
        dietaryPreferenceConclusion = dietaryPreferenceConclusion,
        dietaryRestrictionConclusion = dietaryRestrictionConclusion,
        allergenConclusion = allergenConclusion
    )
}