package com.mdev.feature_product_details.domain.model

import com.mdev.core.utils.round
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.NutrientCategory
import com.mdev.openfoodfacts_client.domain.model.NutrientPreference
import com.mdev.openfoodfacts_client.domain.model.NutrientPreferenceType
import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.domain.model.PointsLevel
import com.mdev.openfoodfacts_client.utils.NutriScoreCalculator

internal class NutrientGenerator(product: com.mdev.openfoodfacts_client.data.remote.dto.ProductDto){
    private var nutrients : MutableList<Nutrient>
        init {
            val productNutrients = product.nutrients
            val nutriScoreData = product.nutriScoreData
            val sugar = Nutrient.getNutrient(
                nutrientType = NutrientType.SUGAR,
                points = nutriScoreData?.sugarsPoints ?: NutriScoreCalculator.getPoints(
                    NutrientType.SUGAR,
                    productNutrients?.sugars100g
                ), contentPerHundredGrams = productNutrients?.sugars100g?.round() ?: 0,
                servingUnit = productNutrients?.sugarsUnit ?: ""
            )
            val energy = Nutrient.getNutrient(
                nutrientType = NutrientType.ENERGY,
                points = nutriScoreData?.energyPoints ?: NutriScoreCalculator.getPoints(
                    NutrientType.ENERGY,
                    productNutrients?.energy?.toDouble()
                ), contentPerHundredGrams = productNutrients?.energyKcal100g?.round() ?: 0,
                servingUnit = productNutrients?.energyKcalUnit ?: ""
            )
            val saturates = Nutrient.getNutrient(
                nutrientType = NutrientType.SATURATES,
                points = nutriScoreData?.saturatedFatPoints ?: NutriScoreCalculator.getPoints(
                    NutrientType.SATURATES, productNutrients?.saturatedFat100g
                ), contentPerHundredGrams = productNutrients?.saturatedFat100g?.round() ?: 0,
                servingUnit = productNutrients?.saturatedFatUnit ?: ""
            )
            val fibre = Nutrient.getNutrient(
                nutrientType = NutrientType.FIBRE,
                points = nutriScoreData?.fiberPoints ?: NutriScoreCalculator.getPoints(
                    NutrientType.FIBRE,
                    productNutrients?.fiber100g
                ), contentPerHundredGrams = productNutrients?.fiber100g?.round() ?: 0,
                servingUnit = productNutrients?.fiberUnit ?: ""
            )
            val sodiumContentInMg = NutriScoreCalculator.getMgFromGram(productNutrients?.sodium100g)
            val sodium = Nutrient.getNutrient(
                nutrientType = NutrientType.SODIUM,
                points = nutriScoreData?.sodiumPoints
                    ?: NutriScoreCalculator.getPoints(NutrientType.SODIUM, sodiumContentInMg),
                contentPerHundredGrams = sodiumContentInMg.round(),
                servingUnit = "mg"
            )
            val fruitsVegetablesAndNuts = Nutrient.getNutrient(
                nutrientType = NutrientType.FRUITS_VEGETABLES_AND_NUTS,
                points = nutriScoreData?.fruitsVegetablesNutsColzaWalnutOliveOilsPoints
                    ?: NutriScoreCalculator.getPoints(
                        NutrientType.FRUITS_VEGETABLES_AND_NUTS,
                        productNutrients?.fruitsVegetablesNutsEstimateFromIngredients100g
                    ),
                contentPerHundredGrams = productNutrients?.fruitsVegetablesNutsEstimateFromIngredients100g?.round()
                    ?: 0,
                servingUnit = "%"
            )
            val protein = Nutrient.getNutrient(
                nutrientType = NutrientType.PROTEIN,
                points = nutriScoreData?.proteinsPoints ?: NutriScoreCalculator.getPoints(
                    NutrientType.PROTEIN, productNutrients?.proteins100g
                ), contentPerHundredGrams = productNutrients?.proteins100g?.round() ?: 0,
                servingUnit = productNutrients?.proteinsUnit ?: ""
            )
            nutrients = mutableListOf(
               sugar,
               energy,
               saturates,
               fibre,
               sodium,
               fruitsVegetablesAndNuts,
               protein,
           )

        }
    fun generateNutrientsForView(nutrientCategory: NutrientCategory) : List<Nutrient>{
            return nutrients.filter{ it.nutrientCategory == nutrientCategory}
    }
    fun getNutrientPreference(): List<NutrientPreference>{
        val nutrientPreferences = mutableListOf<NutrientPreference>()
        nutrients.forEach { nutrient ->
            nutrientPreferences.add(
                NutrientPreference(
                    nutrientType = nutrient.nutrientType,
                    nutrientPreferenceType = getNutrientPreferenceTypeFromPointsLevel(nutrient.pointsLevel)
                )
            )
        }
        return nutrientPreferences
    }
}


class Nutrient(
    val nutrientType: NutrientType,
    val contentPerHundredGrams: Number,
    val description: String,
    val pointsLevel: PointsLevel,
    val nutrientCategory: NutrientCategory,
    val healthCategory: HealthCategory,
    val servingUnit: String
){
    companion object{
        fun getNutrient(nutrientType: NutrientType, points: Int, contentPerHundredGrams: Number, servingUnit: String) : Nutrient {
            val (pointsLevel , healthCategory) =  getNutriScoreAndPointsCategory(nutrientType= nutrientType,points = points)
            val nutrientCategory = getNutrientCategory(nutrientType = nutrientType, points = points)
            return Nutrient(
                nutrientType = nutrientType,
                contentPerHundredGrams = contentPerHundredGrams,
                description = "${pointsLevel.description} ${nutrientType.description}",
                nutrientCategory = nutrientCategory,
                pointsLevel = pointsLevel,
                healthCategory = healthCategory,
                servingUnit = servingUnit
            )
        }


        private fun getNutrientCategory(nutrientType: NutrientType, points: Int): NutrientCategory {
            return when(nutrientType){
                NutrientType.ENERGY,
                NutrientType.SATURATES,
                NutrientType.SUGAR,
                NutrientType.SODIUM
                -> {
                    when(points){
                        0,1,2,3,4,5 -> NutrientCategory.POSITIVE
                        6,7,8,9,10 -> NutrientCategory.NEGATIVE
                        else -> NutrientCategory.UNKNOWN
                    }
                }
                NutrientType.FIBRE,
                NutrientType.PROTEIN,
                NutrientType.FRUITS_VEGETABLES_AND_NUTS
                -> {
                    when(points){
                        in 0 until 6 -> NutrientCategory.POSITIVE
                        else -> NutrientCategory.UNKNOWN
                    }
                }
            }
        }

    }
}
//
//Negative Nutrients (Energy, Saturated Fat, Sugars, Sodium)
//Too Low: 0-1 points
//Low: 2-3 points
//Moderate: 4-5 points
//High: 6-7 points
//Too High: 8-10 points
//Positive Nutrients (Fiber, Proteins, Fruits, Vegetables, Nuts, Colza, Walnut, Olive Oils)
//Too Low: 0 points
//Low: 1 point
//Moderate: 2-3 points
//High: 4 points
//Too High: 5 points


fun getNutriScoreAndPointsCategory(nutrientType: NutrientType, points : Int) : Pair<PointsLevel, HealthCategory>{
    return when(nutrientType){
        NutrientType.ENERGY,
        NutrientType.SATURATES,
        NutrientType.SUGAR,
        NutrientType.SODIUM
        -> when(points){
            0,1 -> Pair(PointsLevel.TOO_LOW, HealthCategory.HEALTHY)
            2,3 -> Pair(PointsLevel.LOW, HealthCategory.GOOD)
            4,5 -> Pair(PointsLevel.MODERATE, HealthCategory.FAIR)
            6,7 -> Pair(PointsLevel.HIGH, HealthCategory.POOR)
            8,9,10 -> Pair(PointsLevel.TOO_HIGH, HealthCategory.BAD)
            else -> Pair(PointsLevel.UNKNOWN, HealthCategory.UNKNOWN)
        }
        NutrientType.PROTEIN,
        NutrientType.FIBRE,
        NutrientType.FRUITS_VEGETABLES_AND_NUTS
            -> {
                when(points) {
                    0 -> Pair(PointsLevel.TOO_LOW, HealthCategory.GOOD)
                    1 -> Pair(PointsLevel.LOW, HealthCategory.GOOD)
                    2,3 -> Pair(PointsLevel.MODERATE, HealthCategory.GOOD)
                    4 -> Pair(PointsLevel.HIGH, HealthCategory.HEALTHY)
                    5 -> Pair(PointsLevel.TOO_HIGH, HealthCategory.HEALTHY)
                   else -> Pair(PointsLevel.UNKNOWN, HealthCategory.UNKNOWN)

               }
            }
    }
}
fun getNutrientPreferenceTypeFromPointsLevel(pointsLevel: PointsLevel) : NutrientPreferenceType {
   return when(pointsLevel){
       PointsLevel.TOO_LOW -> NutrientPreferenceType.LOW
       PointsLevel.LOW -> NutrientPreferenceType.LOW
       PointsLevel.MODERATE -> NutrientPreferenceType.MODERATE
       PointsLevel.HIGH -> NutrientPreferenceType.HIGH
       PointsLevel.TOO_HIGH -> NutrientPreferenceType.HIGH
       PointsLevel.UNKNOWN -> NutrientPreferenceType.UNKNOWN
   }
}
