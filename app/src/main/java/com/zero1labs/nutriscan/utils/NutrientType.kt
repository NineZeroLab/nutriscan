package com.zero1labs.nutriscan.utils

enum class NutrientType (val description: String,val heading: String){
    ENERGY ("energy","Calories"),
    PROTEIN ("protein", "Protein"),
    SATURATES ("saturated fat","Saturated fat"),
    SUGAR ("sugar", "Sugar"),
    FIBRE ("fibre", "Fibre"),
    SODIUM ("salt", "Sodium"),
    FRUITS_VEGETABLES_AND_NUTS("fruits,vegetables and nuts" , "Fruits, Veggies and Nuts")
}