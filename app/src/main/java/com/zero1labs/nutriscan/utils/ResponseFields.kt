package com.zero1labs.nutriscan.utils

object ResponseFields {
    private val fields = mutableListOf<String>(
        "code",
        "product_name",
        "image_url",
        "nutriments",
        "ingredients",
        "nutriscore_data",
        "brands",
        "nutriscore_grade",
        "categories_hierarchy",
        "allergens_hierarchy",
        "ingredients_analysis_tags",
        "additives_original_tags"
    )

    fun getFields(): String{
        return fields.joinToString(",")
    }
}