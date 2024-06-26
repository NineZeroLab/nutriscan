package com.zero1labs.nutriscan.utils

import com.zero1labs.nutriscan.data.models.NutrientType

class NutriScoreCalculator {

    // Function to get Points from nutrientType for servingPer100gm
    companion object{
        fun getPoints(nutrientType: NutrientType, servingPerHundredGrams : Double?) : Int {
            if (servingPerHundredGrams == null) return -1
            else return when(nutrientType){
                NutrientType.ENERGY -> {
                    if (servingPerHundredGrams <= 335)  0
                    else if (servingPerHundredGrams < 670) 1
                    else if (servingPerHundredGrams < 1005) 2
                    else if (servingPerHundredGrams < 1340) 3
                    else if (servingPerHundredGrams < 1675) 4
                    else if (servingPerHundredGrams < 2010) 5
                    else if (servingPerHundredGrams < 2345) 6
                    else if (servingPerHundredGrams < 2680) 7
                    else if (servingPerHundredGrams < 3015) 8
                    else if (servingPerHundredGrams < 3350) 9
                    else if (servingPerHundredGrams >= 3350) 10
                        else -1
                }
                NutrientType.SUGAR -> {
                    if (servingPerHundredGrams <= 4.5)  0
                    else if (servingPerHundredGrams < 9) 1
                    else if (servingPerHundredGrams < 13.5) 2
                    else if (servingPerHundredGrams < 18) 3
                    else if (servingPerHundredGrams < 22.5) 4
                    else if (servingPerHundredGrams < 27) 5
                    else if (servingPerHundredGrams < 31) 6
                    else if (servingPerHundredGrams < 36) 7
                    else if (servingPerHundredGrams < 40) 8
                    else if (servingPerHundredGrams < 45) 9
                    else if (servingPerHundredGrams >= 45) 10
                    else -1
                }
                NutrientType.SATURATES -> {
                    if (servingPerHundredGrams <= 1)  0
                    else if (servingPerHundredGrams <= 2) 1
                    else if (servingPerHundredGrams <= 3) 2
                    else if (servingPerHundredGrams <= 4) 3
                    else if (servingPerHundredGrams <= 5) 4
                    else if (servingPerHundredGrams <= 6) 5
                    else if (servingPerHundredGrams <= 7) 6
                    else if (servingPerHundredGrams <= 8) 7
                    else if (servingPerHundredGrams <= 9) 8
                    else if (servingPerHundredGrams <= 10) 9
                    else if (servingPerHundredGrams > 10) 10
                    else -1
                }
                NutrientType.SODIUM -> {
                    if (servingPerHundredGrams <= 90)  0
                    else if (servingPerHundredGrams <= 180) 1
                    else if (servingPerHundredGrams <= 270) 2
                    else if (servingPerHundredGrams <= 360) 3
                    else if (servingPerHundredGrams <= 450) 4
                    else if (servingPerHundredGrams <= 540) 5
                    else if (servingPerHundredGrams <= 630) 6
                    else if (servingPerHundredGrams <= 720) 7
                    else if (servingPerHundredGrams <= 810) 8
                    else if (servingPerHundredGrams <= 900) 9
                    else if (servingPerHundredGrams > 900) 10
                    else -1
                }
                NutrientType.FRUITS_VEGETABLES_AND_NUTS -> {
                    if (servingPerHundredGrams <= 40)  0
                    else if (servingPerHundredGrams <= 60) 1
                    else if (servingPerHundredGrams <= 80) 2
                    else if (servingPerHundredGrams > 80) 5
                    else -1
                }
                NutrientType.FIBRE -> {
                    if (servingPerHundredGrams <= 0.9)  0
                    else if (servingPerHundredGrams <= 1.9) 1
                    else if (servingPerHundredGrams <= 2.8) 2
                    else if (servingPerHundredGrams <= 3.7) 3
                    else if (servingPerHundredGrams <= 4.7) 4
                    else if (servingPerHundredGrams > 4.7) 5
                   else -1
                }
                NutrientType.PROTEIN -> {
                    if (servingPerHundredGrams <= 1.6)  0
                    else if (servingPerHundredGrams <= 3.2) 1
                    else if (servingPerHundredGrams <= 4.8) 2
                    else if (servingPerHundredGrams <= 6.4) 3
                    else if (servingPerHundredGrams <= 8.0) 4
                    else if (servingPerHundredGrams > 8.0) 5
                    else -1
                }
            }
        }
    }
}