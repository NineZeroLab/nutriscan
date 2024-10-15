package com.mdev.openfoodfacts_client.data.repository

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mdev.openfoodfacts_client.R
import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.domain.repository.ProductRepository
import com.mdev.openfoodfacts_client.utils.ResponseFields
import java.io.File
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
            fields = ResponseFields.getFields()
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
}