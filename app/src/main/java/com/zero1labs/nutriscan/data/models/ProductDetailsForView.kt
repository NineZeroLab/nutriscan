package com.zero1labs.nutriscan.data.models

import com.zero1labs.nutriscan.utils.NutriScoreCalculator

sealed class ProductDetailsListItems{
    data class ProductHeader(val mainDetailsForView: MainDetailsForView) : ProductDetailsListItems()
    data class PositiveNutrientsForView(val nutrient : Nutrient) : ProductDetailsListItems()
    data class NegativeNutrientsForView(val nutrient: Nutrient) : ProductDetailsListItems()
    data class NutrientsHeaderForView(val nutrientCategory : NutrientCategory) : ProductDetailsListItems()
}

class MainDetailsForView(
    val imageUrl: String,
    val productName: String,
    val productBrand: String,
    val nutriScoreGrade: String,
    val healthCategory: HealthCategory
){
    companion object{
        fun getMainDetailsForView(product : Product) : MainDetailsForView{
            return MainDetailsForView(
                productName = product.productName,
                productBrand = product.brand,
                imageUrl = product.imageUrl,
                nutriScoreGrade = product.nutriScoreGrade,
                healthCategory = getHealthCategory(product.nutriScoreGrade),
            )
        }
        private fun getHealthCategory(nutriScoreGrade: String) : HealthCategory{
            return when(nutriScoreGrade){
                "a" -> HealthCategory.HEALTHY
                "b" -> HealthCategory.GOOD
                "c" -> HealthCategory.FAIR
                "d" -> HealthCategory.POOR
                "e" -> HealthCategory.BAD
                else -> HealthCategory.UNKNOWN
            }
        }
    }
}

class NutrientGenerator(product: Product){
    private var nutrients : MutableList<Nutrient>
        init {
            val productNutrients = product.nutrients
            val nutriScoreData = product.nutriScoreData
            val sugar = Nutrient.getNutrient(
                nutrientType = NutrientType.SUGAR,
                points = nutriScoreData?.sugarsPoints ?: NutriScoreCalculator.getPoints(NutrientType.SUGAR, productNutrients.sugars100g),
                contentPerHundredGrams = productNutrients.sugars100g ?: 0,
                servingUnit = productNutrients.sugarsUnit ?: ""
            )
            val energy = Nutrient.getNutrient(
                nutrientType = NutrientType.ENERGY,
                points = nutriScoreData?.energyPoints ?: NutriScoreCalculator.getPoints(NutrientType.ENERGY, productNutrients.energyKcal100g),
                contentPerHundredGrams = productNutrients.energyKcal100g ?: 0,
                servingUnit = productNutrients.energyKcalUnit ?: ""
            )
            val saturates = Nutrient.getNutrient(
                nutrientType = NutrientType.SATURATES,
                points = nutriScoreData?.saturatedFatPoints ?: NutriScoreCalculator.getPoints(NutrientType.SATURATES, productNutrients.saturatedFat100g),
                contentPerHundredGrams = productNutrients.saturatedFat100g ?: 0,
                servingUnit =  productNutrients.saturatedFatUnit ?: ""
            )
            val fibre = Nutrient.getNutrient(
                nutrientType = NutrientType.FIBRE,
                points = nutriScoreData?.fiberPoints ?: NutriScoreCalculator.getPoints(NutrientType.FIBRE, productNutrients.fiber100g),
                contentPerHundredGrams = productNutrients.fiber100g ?: 0,
                servingUnit = productNutrients.fiberUnit ?: ""
            )
            val sodium = Nutrient.getNutrient(
                nutrientType = NutrientType.SODIUM,
                points = nutriScoreData?.sodiumPoints ?: NutriScoreCalculator.getPoints(NutrientType.SODIUM, productNutrients.fiber100g),
                contentPerHundredGrams = productNutrients.sodium100g ?: 0,
                servingUnit = productNutrients.sodiumUnit ?: ""
            )
            val fruitsVegetablesAndNuts = Nutrient.getNutrient(
                nutrientType = NutrientType.FRUITS_VEGETABLES_AND_NUTS,
                points = nutriScoreData?.fruitsVegetablesNutsColzaWalnutOliveOilsPoints ?: NutriScoreCalculator.getPoints(NutrientType.FRUITS_VEGETABLES_AND_NUTS, productNutrients.fruitsVegetablesNutsEstimateFromIngredients100g),
                contentPerHundredGrams = productNutrients.fruitsVegetablesNutsEstimateFromIngredients100g ?: 0,
                servingUnit = "%" //TODO: replace hard value (found it to be "%" per 100g in page 28 of QR_Nutri-Score_EN.pdf (Yuka))
            )
            val protein = Nutrient.getNutrient(
                nutrientType = NutrientType.PROTEIN,
                points = nutriScoreData?.proteinsPoints ?: NutriScoreCalculator.getPoints(NutrientType.PROTEIN, productNutrients.proteins100g),
                contentPerHundredGrams = productNutrients.proteins100g ?: 0,
                servingUnit = productNutrients.proteinsUnit ?: ""
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

    fun getNutrientsCount(nutrientCategory: NutrientCategory) : Int{
        return generateNutrientsForView(nutrientCategory).size
    }
}

enum class HealthCategory(val description: String){
    HEALTHY("Healthy"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor"),
    BAD("Bad"),
    UNKNOWN("Unknown")
}

enum class PointsCategory (val description: String){
    TOO_LOW ("too low"),
    LOW ("low"),
    MODERATE ("moderate"),
    HIGH ("high"),
    TOO_HIGH ("too high"),
    UNKNOWN ("unknown")
}

enum class NutrientCategory(val header : String){
    POSITIVE ("Positives"),
    NEGATIVE ("Negatives"),
    UNKNOWN ("Unknown")
}

enum class NutrientType (val description: String,val heading: String){
    ENERGY ("energy","Calories"),
    PROTEIN ("protein", "Protein"),
    SATURATES ("saturated fat","Saturated fat"),
    SUGAR ("sugar", "Sugar"),
    FIBRE ("fibre", "Fibre"),
    SODIUM ("salt", "Sodium"),
    FRUITS_VEGETABLES_AND_NUTS("fruits,vegetables and nuts" , "Fruits, Veggies and Nuts")
}


class Nutrient(
    val nutrientType: NutrientType,
    val points: Int,
    val contentPerHundredGrams : Number,
    val pointsCategory: PointsCategory,
    val description : String,
    val nutrientCategory: NutrientCategory,
    val healthCategory: HealthCategory,
    val servingUnit : String
){
    companion object{
        fun getNutrient(nutrientType: NutrientType, points: Int, contentPerHundredGrams: Number, servingUnit: String) : Nutrient{
            val (pointsCategory , healthCategory) =  getNutriScoreAndPointsCategory(nutrientType= nutrientType,points = points)
            val nutrientCategory = getNutrientCategory(nutrientType = nutrientType, points = points)
            return Nutrient(
                nutrientType = nutrientType,
                points = points,
                contentPerHundredGrams = contentPerHundredGrams,
                pointsCategory = pointsCategory,
                nutrientCategory = nutrientCategory,
                healthCategory = healthCategory,
                description = "${pointsCategory.description} ${nutrientType.description}",
                servingUnit = servingUnit
            )
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


fun getNutriScoreAndPointsCategory(nutrientType: NutrientType, points : Int) : Pair<PointsCategory,HealthCategory>{
    return when(nutrientType){
        NutrientType.ENERGY,
        NutrientType.SATURATES,
        NutrientType.SUGAR,
        NutrientType.SODIUM
        -> {
            return when(points){
                0,1 -> Pair(PointsCategory.TOO_LOW, HealthCategory.HEALTHY)
                2,3 -> Pair(PointsCategory.LOW, HealthCategory.GOOD)
                4,5 -> Pair(PointsCategory.MODERATE, HealthCategory.FAIR)
                6,7 -> Pair(PointsCategory.HIGH, HealthCategory.POOR)
                8,9,10 -> Pair(PointsCategory.TOO_HIGH, HealthCategory.BAD)
                else -> Pair(PointsCategory.UNKNOWN,HealthCategory.UNKNOWN)
            }
        }
        NutrientType.PROTEIN,
        NutrientType.FIBRE,
        NutrientType.FRUITS_VEGETABLES_AND_NUTS
            -> {
                return when(points) {
                    0 -> Pair(PointsCategory.TOO_LOW, HealthCategory.GOOD)
                    1 -> Pair(PointsCategory.LOW, HealthCategory.GOOD)
                    2,3 -> Pair(PointsCategory.MODERATE, HealthCategory.GOOD)
                    4 -> Pair(PointsCategory.HIGH, HealthCategory.HEALTHY)
                    5 -> Pair(PointsCategory.TOO_HIGH, HealthCategory.HEALTHY)
                   else -> Pair(PointsCategory.UNKNOWN, HealthCategory.UNKNOWN)

               }
            }
    }
}

fun getNutrientCategory(nutrientType: NutrientType, points: Int): NutrientCategory{
    return when(nutrientType){
        NutrientType.ENERGY,
        NutrientType.SATURATES,
        NutrientType.SUGAR,
        NutrientType.SODIUM
            -> {
               return when(points){
                   0,1,2,3,4,5 -> NutrientCategory.POSITIVE
                   6,7,8,9,10 -> NutrientCategory.NEGATIVE
                   else -> NutrientCategory.UNKNOWN
               }
            }
        NutrientType.FIBRE,
        NutrientType.PROTEIN,
        NutrientType.FRUITS_VEGETABLES_AND_NUTS
            -> {
                return when(points){
                    in 0 until 6 -> NutrientCategory.POSITIVE
                    else -> NutrientCategory.UNKNOWN
                }
            }
    }
}

fun getNutriScoreCategory(nutrientType: NutrientType,pointsCategory: PointsCategory) : HealthCategory {
    return when(nutrientType){
        NutrientType.ENERGY,
        NutrientType.SATURATES,
        NutrientType.SUGAR,
        NutrientType.SODIUM
            -> {
                return when(pointsCategory){
                    PointsCategory.TOO_LOW -> HealthCategory.HEALTHY
                    PointsCategory.LOW -> HealthCategory.GOOD
                    PointsCategory.MODERATE -> HealthCategory.FAIR
                    PointsCategory.HIGH -> HealthCategory.POOR
                    PointsCategory.TOO_HIGH -> HealthCategory.BAD
                    PointsCategory.UNKNOWN -> HealthCategory.UNKNOWN
                }
            }

        NutrientType.PROTEIN,
        NutrientType.FIBRE ,
        NutrientType.FRUITS_VEGETABLES_AND_NUTS
            -> {
                return when(pointsCategory){
                    PointsCategory.TOO_LOW -> HealthCategory.GOOD
                    PointsCategory.LOW -> HealthCategory.GOOD
                    PointsCategory.MODERATE -> HealthCategory.GOOD
                    PointsCategory.HIGH ->  HealthCategory.HEALTHY
                    PointsCategory.TOO_HIGH -> HealthCategory.HEALTHY
                    PointsCategory.UNKNOWN -> HealthCategory.UNKNOWN
                }
            }
    }
}
