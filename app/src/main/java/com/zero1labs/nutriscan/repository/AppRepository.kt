package com.zero1labs.nutriscan.repository

import android.util.Log
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.utils.Resource
import com.zero1labs.nutriscan.utils.ResponseFields
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

import javax.inject.Inject


class AppRepository  @Inject constructor(
    private val retrofit: Retrofit
) {


    suspend fun getProductDetailsById(productId : String ): Resource<Product> {
        val apiRequests = retrofit.create(ApiRequests::class.java)
        return withContext(Dispatchers.IO){
            try {
                val response = apiRequests.getProductDetails(productId = productId, fields = ResponseFields.getFields())
                if (response.isSuccessful){
                    Resource.Success(response.body()?.products?.get(0))
                }else{
                    Log.d("logger", "Error: ${response.errorBody()?.string()}")
                    Resource.Error(message = response.errorBody()?.string().toString())
                }

            }catch (e: Exception){
                Log.d("logger", "getProductDetails api call failure ${e.message}")
                Resource.Error(e.message.toString())
            }
        }


    }
}