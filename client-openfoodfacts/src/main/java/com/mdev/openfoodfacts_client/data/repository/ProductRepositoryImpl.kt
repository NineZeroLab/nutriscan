package com.mdev.openfoodfacts_client.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mdev.openfoodfacts_client.R
import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import com.mdev.openfoodfacts_client.utils.ResponseFields
import javax.inject.Inject


internal class ProductRepositoryImpl @Inject constructor (
    private val openFoodFactsApi: OpenFoodFactsApi,
    private val context: Context
): ProductRepository {
    private var _additives: List<AdditiveDto> = mutableListOf()
    init {
        fetchAdditives()
    }
    override suspend fun getProductDetailsById(productId: String): ProductDto? {
        val searchResponse = openFoodFactsApi.getProductDetails(
            productId = productId,
            fields = ResponseFields.getProductDetailsFields()
        )
        return searchResponse.products.getOrNull(0)
    }

    private fun fetchAdditives(){
        val gson = Gson()
        val jsonFile = context.resources.openRawResource(R.raw.additives)
            .bufferedReader()
            .use {
                it.readText()
            }
        val additivesType = object : TypeToken<List<AdditiveDto>>() {}.type
        val additives: List<AdditiveDto> = gson.fromJson(jsonFile, additivesType)
        _additives = additives
    }

    override fun getAdditiveByENumber(eNumber: String): AdditiveDto?{
        return _additives.filter {
            it.eNumber == eNumber
        }.getOrNull(0)
    }

    override suspend fun getRecommendedProducts(
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): List<RecommendedProductDto>? {
        val allergensTags = allergens.joinToString(",") { allergen ->
            allergen.allergenStrings.joinToString(prefix = "-", separator = ",")
        }
        val ingredientAnalysisTags = dietaryRestrictions.joinToString(",") {
            it.response
        }
        val searchResponse = openFoodFactsApi.getRecommendedProducts(
           fields = ResponseFields.getRecommendedProductFields(),
            sortBy = "nutriscore_score",
            allergenTags = allergensTags,
            ingredientAnalysisTags = ingredientAnalysisTags
        )
        return searchResponse.recommendedProductDtos
    }
}