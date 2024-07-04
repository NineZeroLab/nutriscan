package com.zero1labs.nutriscan.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data,message)
}






object AppResources{
    const val BASE_URL : String = "https://world.openfoodfacts.org/api/v2/"
    private const val DEMO_ITEM_ID = "3017620422003"
    private const val BHUJJIA = "011433157933"
    private const val SEASONED_PEANUTS = "059966050932"
    private const val ALOO_LACHHA = "8904004402858"
    private const val COCA_COLA_CAN = "5449000256805"

    const val PRODUCT_NOT_FOUND = "Product Not Found In the Database"
    const val UNABLE_TO_ESTABLISH_CONNECTION = "Unable to establish connection"
    const val UNKNOWN_ERROR = "Unknown Error Occurred"
    const val CONNECTION_TIMEOUT = "Connection Timed Out"

    fun getRandomItem(): String{
        return listOf(
            DEMO_ITEM_ID,
            BHUJJIA,
            SEASONED_PEANUTS,
            ALOO_LACHHA,
            COCA_COLA_CAN,
            "40822099",
            "0049000042559",
            "1123"
        ).random()
    }


    fun getProductType(categories: List<String>?): ProductType {
        if(categories == null) return ProductType.UNKNOWN
        if ("en:beverages" in categories) return ProductType.BEVERAGE
        return ProductType.FOOD
    }

    fun getServingTextFromProductType(productType: ProductType): String{
        return when(productType){
            ProductType.BEVERAGE -> "Per Serving 100ml"
            ProductType.FOOD -> "Per Serving 100gm"
            ProductType.UNKNOWN -> "Per Serving 100gm"
        }
    }
}