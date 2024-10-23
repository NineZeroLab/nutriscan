package com.mdev.feature_product_details.domain.model

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.NutrientPreference
import com.mdev.openfoodfacts_client.domain.model.getAllergens
import com.mdev.openfoodfacts_client.domain.model.getDietaryRestrictions

internal data class Considerations(
    val dietaryPreferences: List<NutrientPreference> = emptyList(),
    val dietaryRestrictions: List<DietaryRestriction> = emptyList(),
    val allergens: List<Allergen> = emptyList()
)

internal fun AppUser.getUserConsideration(): Considerations{
    val dietaryPreferences = this.dietaryPreferences
    val dietaryRestrictions = this.dietaryRestrictions
    val allergens = this.allergens
    return Considerations(
        dietaryPreferences = dietaryPreferences,
        dietaryRestrictions = dietaryRestrictions,
        allergens = allergens
    )
}
internal fun ProductDto.getProductConsiderations(): Considerations{
    val dietaryPreferences = NutrientGenerator(this).getNutrientPreference()
    val dietaryRestrictions = getDietaryRestrictions(ingredientsHierarchy = this.ingredientsAnalysisTags)
    val allergens = getAllergens(this.allergensHierarchy)
    return Considerations(
        dietaryPreferences = dietaryPreferences,
        dietaryRestrictions = dietaryRestrictions,
        allergens = allergens
    )
}