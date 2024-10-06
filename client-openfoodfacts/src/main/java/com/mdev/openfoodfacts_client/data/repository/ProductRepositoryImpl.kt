package com.mdev.openfoodfacts_client.data.repository

import android.util.Log
import com.mdev.core.utils.AppResources
import com.mdev.core.utils.Resource
import com.mdev.openfoodfacts_client.data.remote.OpenFoodFactsApi
import com.mdev.openfoodfacts_client.data.remote.dto.ProductDto
import com.mdev.openfoodfacts_client.repository.ProductRepository
import com.mdev.openfoodfacts_client.utils.ResponseFields
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ProductRepositoryImpl (
    private val openFoodFactsApi: OpenFoodFactsApi
): ProductRepository {
    override suspend fun getProductDetailsById(productId: String): Resource<ProductDto>  {
        try {
            val searchResponse = openFoodFactsApi.getProductDetails(
                productId = productId,
                fields = ResponseFields.getFields()
            )
            val productDto = searchResponse.products.getOrNull(0)
            return Resource.Success(data = productDto)
        }catch (e: Exception){
            return when(e){
                is UnknownHostException -> Resource.Error(message = AppResources.UNABLE_TO_ESTABLISH_CONNECTION)
                is IndexOutOfBoundsException -> Resource.Error(message = AppResources.PRODUCT_NOT_FOUND)
                is SocketTimeoutException -> Resource.Error(message = AppResources.CONNECTION_TIMEOUT)
                else -> Resource.Error(message = AppResources.UNKNOWN_ERROR)
            }
        }
    }
}