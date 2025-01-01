package com.mdev.openfoodfacts_client.domain.model

enum class NutrientType (val description: String,val heading: String, val defaultUnit: String){
    ENERGY ("energy","Calories", "Kcal"),
    PROTEIN ("protein", "Protein", "g"),
    SATURATES ("saturated fat","Saturated fat", "g"),
    SUGAR ("sugar", "Sugar", "g"),
    FIBRE ("fibre", "Fibre", "g"),
    SODIUM ("salt", "Sodium", "mg"),
    FRUITS_VEGETABLES_AND_NUTS("fruits,vegetables and nuts" , "Fruits, Veggies and Nuts", "%")
}