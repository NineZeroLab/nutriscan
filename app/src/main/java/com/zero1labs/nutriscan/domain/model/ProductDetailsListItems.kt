package com.zero1labs.nutriscan.domain.model

import com.zero1labs.nutriscan.utils.NutrientCategory
import com.zero1labs.nutriscan.utils.ProductType

sealed class ProductDetailsListItems{
    data class ProductHeader(val mainDetailsForView: MainDetailsForView) : ProductDetailsListItems()
    data class PositiveNutrientsForView(val nutrient : Nutrient) : ProductDetailsListItems()
    data class NegativeNutrientsForView(val nutrient: Nutrient) : ProductDetailsListItems()
    data class NutrientsHeaderForView(val nutrientCategory : NutrientCategory,val productType: ProductType) : ProductDetailsListItems()
}