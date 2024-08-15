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

    suspend fun getProductDetailsById(productId : String , callback: (Resource<Product>) -> Unit){
        withContext(Dispatchers.IO){
            try {
                val response = apiRequests.getProductDetails(productId = productId, fields = ResponseFields.getFields())
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
}