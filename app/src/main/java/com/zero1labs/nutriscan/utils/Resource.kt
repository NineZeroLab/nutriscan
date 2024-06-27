package com.zero1labs.nutriscan.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data,message)
}



object AppResources{
    const val BASE_URL : String = "https://world.openfoodfacts.org/api/v2/"
    const val DEMO_ITEM_ID: String = "3017620422003"
    const val BHUJJIA: String = "011433157933"
    const val SEASONED_PEANUTS = "059966050932"
    const val ALOO_LACHHA = "8904004402858"


    fun getRandomItem(): String{
        return listOf(
            DEMO_ITEM_ID,
            BHUJJIA,
            SEASONED_PEANUTS,
            ALOO_LACHHA,
        ).random()
    }
}