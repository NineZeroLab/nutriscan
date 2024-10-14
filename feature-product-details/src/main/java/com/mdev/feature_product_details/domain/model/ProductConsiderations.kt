package com.mdev.feature_product_details.domain.model

import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.NutrientPreference
import com.mdev.openfoodfacts_client.domain.model.getAllergens
import com.mdev.openfoodfacts_client.domain.model.getDietaryRestrictions

internal data class ProductConsiderations(
    val dietaryPreferences: List<NutrientPreference>,
    val dietaryRestrictions: List<DietaryRestriction>,
    val allergens: List<Allergen>
)


internal fun ProductDto.getProductConsiderations(): ProductConsiderations{
    val dietaryPreferences = NutrientGenerator(this).getNutrientPreference()
    val dietaryRestrictions = getDietaryRestrictions(ingredientsHierarchy = this.ingredientsAnalysisTags)
    val allergens = getAllergens(this.allergensHierarchy)
    return ProductConsiderations(
        dietaryPreferences = dietaryPreferences,
        dietaryRestrictions = dietaryRestrictions,
        allergens = allergens
    )
}