package com.mdev.openfoodfacts_client.utils

import com.mdev.openfoodfacts_client.domain.model.ProductType


object ClientResources{
    const val TAG = "logger"
    private const val DEMO_ITEM_ID = "3017620422003"
    private const val BHUJJIA = "011433157933"
    private const val SEASONED_PEANUTS = "059966050932"
    private const val ALOO_LACHHA = "8904004402858"
    private const val COCA_COLA_CAN = "5449000256805"

    const val PRODUCT_NOT_FOUND = "Product Not Found In the Database"
    const val UNABLE_TO_ESTABLISH_CONNECTION = "Unable to establish connection"
    const val UNKNOWN_ERROR = "Unknown Error Occurred"
    const val CONNECTION_TIMEOUT = "Connection Timed Out"

    const val INVALID_USERNAME_PASSWORD = "Invalid Username or Password. Try Again."

    const val USER_DETAILS_UPDATED = "User Details Updated Successfully"
    const val USER_DETAILS_UPDATE_FAILURE = "Update failed "

    const val USERS = "users"


    fun getRandomItem(): String{
        return listOf(
            DEMO_ITEM_ID,
            BHUJJIA,
            SEASONED_PEANUTS,
            ALOO_LACHHA,
            COCA_COLA_CAN,
//            "40822099",
//            "0049000042559",
            "1123",
            "20724696",
            "3046920028004",
            "3168930009078",
            "87157277",
            "7622300489434",
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
