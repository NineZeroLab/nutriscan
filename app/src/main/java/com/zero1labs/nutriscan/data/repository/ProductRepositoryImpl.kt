package com.zero1labs.nutriscan.data.repository

import android.util.Log
import com.zero1labs.nutriscan.data.remote.OpenFoodFactsApi
import com.zero1labs.nutriscan.data.remote.dto.ProductDto
import com.zero1labs.nutriscan.domain.repository.ProductRepository
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.Resource
import com.zero1labs.nutriscan.utils.ResponseFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import javax.inject.Inject


class ProductRepositoryImpl  @Inject constructor(
    private val openFoodFactsApi: OpenFoodFactsApi
): ProductRepository {
    suspend fun getProductDetailsById(productId : String , callback: (Resource<ProductDto>) -> Unit){
        withContext(Dispatchers.IO){
            try {
                val response = openFoodFactsApi.getProductDetails(productId = productId, fields = ResponseFields.getFields())
                Log.d("logger", "Getting response from api")
                if (response.isSuccessful){
                    Log.d("logger", "response successful")
                    val product = response.body()?.products?.getOrNull(0)
                    if (product != null){
                        callback(Resource.Success(product))
                    } else{
                        Log.d("logger", "Product Not Found")
                        callback(Resource.Error(message = AppResources.PRODUCT_NOT_FOUND))
                    }
                }else{
                    Log.d("logger", "response failed")
                    response.errorBody()?.close()
                    Log.d("logger", "Error: ${response.errorBody()?.string()}")
                    callback(Resource.Error(message = response.errorBody()?.string().toString()))
                }

            }catch (e: Exception){
                Log.d("logger", "Error: $e")
                when(e){
                    is UnknownHostException -> callback(Resource.Error(message = AppResources.UNABLE_TO_ESTABLISH_CONNECTION))
                    is IndexOutOfBoundsException -> callback(Resource.Error(message = AppResources.PRODUCT_NOT_FOUND))
                    is SocketTimeoutException -> callback(Resource.Error(message = AppResources.CONNECTION_TIMEOUT))
                    else -> callback(Resource.Error(message = AppResources.UNKNOWN_ERROR))
                }
            }
        }
    }

    override suspend fun getProductDetailsById(productId: String): Flow<Resource<ProductDto>> = flow {
            try {
                val searchResponse = openFoodFactsApi.getProductDetails(
                    productId = productId,
                    fields = ResponseFields.getFields()
                )
                val productDto = searchResponse.products.getOrNull(0)
                emit(Resource.Success(data = productDto))
            }catch (e: Exception){
                when(e){
                    is UnknownHostException -> emit(Resource.Error(message = AppResources.UNABLE_TO_ESTABLISH_CONNECTION))
                    is IndexOutOfBoundsException -> emit(Resource.Error(message = AppResources.PRODUCT_NOT_FOUND))
                    is SocketTimeoutException -> emit(Resource.Error(message = AppResources.CONNECTION_TIMEOUT))
                    else -> emit(Resource.Error(message = AppResources.UNKNOWN_ERROR))
                }
            }
        }

}