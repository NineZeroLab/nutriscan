package com.zero1labs.nutriscan.utils

object ResponseFields {
    private val fields = mutableListOf<String>(
        "code",
        "product_name",
        "image_url",
        "nutriments",
        "ingredients",
        "nutriscore_data",
    )

    fun getFields(): String{
        return fields.joinToString(",")
    }
}