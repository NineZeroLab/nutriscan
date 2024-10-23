package com.mdev.feature_product_details.domain.model

import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.model.NutrientCategory
import com.mdev.openfoodfacts_client.domain.model.ProductType
import com.mdev.openfoodfacts_client.utils.ClientResources

internal data class ProductDetailsForView(
    val productDetails: ProductDetails,
    val productConsiderations: Considerations,
    val userConsiderations: Considerations,
)

internal data class ProductDetails(
    val mainDetailsForView: MainDetailsForView,
    val positiveNutrients: List<Nutrient>,
    val negativeNutrients: List<Nutrient>,
    val additives: List<AdditivesShortView>,
    val productType: ProductType = ProductType.UNKNOWN,
)


internal fun ProductDto.toProductDetails(): ProductDetails{
    val mainDetailsForView = MainDetailsForView.getMainDetailsForView(this)
    val negativeNutrients = NutrientGenerator(this).generateNutrientsForView(NutrientCategory.NEGATIVE)
    val positiveNutrients = NutrientGenerator(this).generateNutrientsForView(NutrientCategory.POSITIVE)
    val productType = ClientResources.getProductType(this.categoriesHierarchy)

    return ProductDetails(
       mainDetailsForView = mainDetailsForView,
       positiveNutrients =  positiveNutrients,
       negativeNutrients =  negativeNutrients,
       additives = getDemoAdditivesShortView(),
       productType =  productType
    )
}