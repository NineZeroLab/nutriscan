package com.zero1labs.nutriscan.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data,message)
}



object AppResources{
    const val BASE_URL : String = "https://world.openfoodfacts.org/api/v2/"
}