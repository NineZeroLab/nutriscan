package com.zero1labs.nutriscan.utils

class AdditivesCalculator {

    companion object{
        fun parseAdditive(additiveString : String) : String{
            return additiveString.split(":")[1]
        }


    }
}

data class Additive(
    val name : String,
    val description : String,
    val category: AdditiveCategory
)

enum class AdditiveCategory (val description: String){
    HAZARDOUS("Hazardous"),
    LIMITED_RISK ("Limited Risk"),
    RISK_FREE ("Risk-free")
}
