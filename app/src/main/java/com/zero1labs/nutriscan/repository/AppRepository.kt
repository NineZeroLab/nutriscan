package com.zero1labs.nutriscan.repository

import android.util.Log
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.Resource
import com.zero1labs.nutriscan.utils.ResponseFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import javax.inject.Inject


class AppRepository  @Inject constructor(
    private val retrofit: Retrofit
) {

    private val apiRequests = retrofit.create(ApiRequests::class.java)

    suspend fun getProductDetailsById(productId : String ): Resource<Product> {
        return withContext(Dispatchers.IO){
            try {
                val response = apiRequests.getProductDetails(productId = productId, fields = ResponseFields.getFields())
                Log.d("logger", "Getting response from api")
                if (response.isSuccessful){
                    Log.d("logger", "response successful")
                    val product = response.body()?.products?.getOrNull(0)
                    if (product != null){
                        Resource.Success(product)
                    } else{
                        Log.d("logger", "Product Not Found")
                        Resource.Error<Product>(message = AppResources.PRODUCT_NOT_FOUND)
                    }
                }else{
                    Log.d("logger", "response failed")
                    response.errorBody()?.close()
                    Log.d("logger", "Error: ${response.errorBody()?.string()}")
                    Resource.Error(message = response.errorBody()?.string().toString())
                }

            }catch (e: Exception){
                Log.d("logger", "Error: $e")
                when(e){
                    is UnknownHostException -> Resource.Error<Product>(message = AppResources.UNABLE_TO_ESTABLISH_CONNECTION)
                    is IndexOutOfBoundsException -> Resource.Error<Product>(message = AppResources.PRODUCT_NOT_FOUND)
                    is SocketTimeoutException -> Resource.Error<Product>(message = AppResources.CONNECTION_TIMEOUT)
                    else -> Resource.Error<Product>(message = AppResources.UNKNOWN_ERROR)
                }
            }
        }
    }
}