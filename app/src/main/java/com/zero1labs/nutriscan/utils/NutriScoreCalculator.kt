package com.zero1labs.nutriscan.utils

import com.zero1labs.nutriscan.data.models.Nutrients

class NutriScoreCalculator {

    // Function to get Points from nutrientType for servingPer100gm
    companion object{
        fun getPoints(nutrientType: NutrientType, servingPerHundredGrams : Double?) : Int {
            if (servingPerHundredGrams == null) return -1
            else return when(nutrientType){
                NutrientType.ENERGY -> when {
                    servingPerHundredGrams <= 335 ->  0
                    servingPerHundredGrams < 670 -> 1
                    servingPerHundredGrams < 1005 -> 2
                    servingPerHundredGrams < 1340 -> 3
                    servingPerHundredGrams < 1675 -> 4
                    servingPerHundredGrams < 2010 -> 5
                    servingPerHundredGrams < 2345 -> 6
                    servingPerHundredGrams < 2680 -> 7
                    servingPerHundredGrams < 3015 -> 8
                    servingPerHundredGrams < 3350 -> 9
                    servingPerHundredGrams >= 3350 -> 10
                        else -> -1
                }
                NutrientType.SUGAR ->  when {
                    servingPerHundredGrams <= 4.5 ->  0
                    servingPerHundredGrams < 9 -> 1
                    servingPerHundredGrams < 13.5 ->  2
                    servingPerHundredGrams < 18 -> 3
                    servingPerHundredGrams < 22.5 -> 4
                    servingPerHundredGrams < 27 ->  5
                    servingPerHundredGrams < 31 -> 6
                    servingPerHundredGrams < 36 -> 7
                    servingPerHundredGrams < 40 -> 8
                    servingPerHundredGrams < 45 -> 9
                    servingPerHundredGrams >= 45 -> 10
                    else  -> -1
                }
                NutrientType.SATURATES -> when {
                    servingPerHundredGrams <= 1 ->  0
                    servingPerHundredGrams <= 2 -> 1
                    servingPerHundredGrams <= 3 -> 2
                    servingPerHundredGrams <= 4 -> 3
                    servingPerHundredGrams <= 5 -> 4
                    servingPerHundredGrams <= 6 -> 5
                    servingPerHundredGrams <= 7 -> 6
                    servingPerHundredGrams <= 8 -> 7
                    servingPerHundredGrams <= 9 -> 8
                    servingPerHundredGrams <= 10 -> 9
                    servingPerHundredGrams > 10 -> 10
                    else -> -1
                }
                NutrientType.SODIUM -> when {
                    servingPerHundredGrams <= 90 -> 0
                    servingPerHundredGrams <= 180 -> 1
                    servingPerHundredGrams <= 270 -> 2
                    servingPerHundredGrams <= 360 -> 3
                    servingPerHundredGrams <= 450 -> 4
                    servingPerHundredGrams <= 540 -> 5
                    servingPerHundredGrams <= 630 -> 6
                    servingPerHundredGrams <= 720 -> 7
                    servingPerHundredGrams <= 810 -> 8
                    servingPerHundredGrams <= 900 -> 9
                    servingPerHundredGrams >  900 -> 10
                    else -> -1
                }
                NutrientType.FRUITS_VEGETABLES_AND_NUTS -> when {
                    servingPerHundredGrams <= 40 ->  0
                    servingPerHundredGrams <= 60 -> 1
                    servingPerHundredGrams <= 80 -> 2
                    servingPerHundredGrams > 80 -> 5
                    else -> -1
                }
                NutrientType.FIBRE -> when {
                    servingPerHundredGrams <= 0.9 ->  0
                    servingPerHundredGrams <= 1.9 -> 1
                    servingPerHundredGrams <= 2.8 -> 2
                    servingPerHundredGrams <= 3.7 -> 3
                    servingPerHundredGrams <= 4.7 -> 4
                    servingPerHundredGrams > 4.7 -> 5
                   else -> -1
                }
                NutrientType.PROTEIN -> when{
                    servingPerHundredGrams <= 1.6 ->  0
                    servingPerHundredGrams <= 3.2 -> 1
                    servingPerHundredGrams <= 4.8 -> 2
                    servingPerHundredGrams <= 6.4 -> 3
                    servingPerHundredGrams <= 8.0 -> 4
                    servingPerHundredGrams > 8.0 -> 5
                    else -> -1
                }
            }
        }

        fun getNutriScoreGrade(nutrients: Nutrients) : String {

//        Total Negative Points=Energy Points+Sugars Points+Saturated Fat Points+Sodium Points
//        Total Positive Points=Fruit/Vegetable Points+Fiber Points+Protein Points
//        Nutri-Score=Total Negative Points−Total Positive Points

            val negativePoints =
                        getPoints(NutrientType.ENERGY, nutrients.energyKcal100g) +
                        getPoints(NutrientType.SUGAR, nutrients.sugars100g) +
                        getPoints(NutrientType.SATURATES, nutrients.saturatedFat100g) +
                        getPoints(NutrientType.SODIUM, nutrients.sodium100g)

            val positivePoints =
                        getPoints(NutrientType.FRUITS_VEGETABLES_AND_NUTS, nutrients.fruitsVegetablesNutsEstimateFromIngredients100g) +
                        getPoints(NutrientType.FIBRE, nutrients.fiber100g) +
                        getPoints(NutrientType.PROTEIN, nutrients.proteins100g)

            val nutriScore = negativePoints - positivePoints

            return getNutriScoreCharacter(nutriScore)


        }

        private fun getNutriScoreCharacter(nutriScore : Int) : String {
            return when {
                nutriScore <= -1 -> "a"
                nutriScore <= 2 -> "b"
                nutriScore <= 10 -> "c"
                nutriScore <= 18 -> "d"
                else -> "e"
            }
        }
    }
}