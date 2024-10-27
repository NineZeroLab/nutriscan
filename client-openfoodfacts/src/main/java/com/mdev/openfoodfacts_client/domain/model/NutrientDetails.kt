package com.mdev.openfoodfacts_client.domain.model

import com.mdev.openfoodfacts_client.data.remote.dto.NutriScoreDataDto
import com.mdev.openfoodfacts_client.data.remote.dto.NutrientsDto
import com.mdev.openfoodfacts_client.utils.NutriScoreCalculator


data class NutrientDetails(
    val nutrientType: NutrientType? = null,
    val contentPerHundredGram: Number = 0,
    val description: String = "",
    val pointsLevel: PointsLevel = PointsLevel.UNKNOWN,
    val nutrientCategory: NutrientCategory = NutrientCategory.UNKNOWN,
    val healthCategory: HealthCategory,
    val servingUnit: String,
){
    companion object{
        fun fromDtoAttributes(
            nutrientType: NutrientType,
            points: Int,
            contentPerHundredGram: Number,
            servingUnit: String
        ): NutrientDetails{
            val (pointsLevel, healthCategory) = nutrientType.getNutriScoreAndPointsCategory(points)
            val nutrientCategory = nutrientType.getNutrientCategory(points)

            return NutrientDetails(
                nutrientType = nutrientType,
                contentPerHundredGram = contentPerHundredGram,
                description = "${pointsLevel.description} ${nutrientType.description} ",
                pointsLevel = pointsLevel,
                nutrientCategory = nutrientCategory,
                healthCategory = healthCategory,
                servingUnit = servingUnit
            )
        }
    }
}




fun NutrientsDto.toNutrientDetailsList(nutriScoreDataDto: NutriScoreDataDto? = null): List<NutrientDetails>{
    val nutrients = mutableListOf<NutrientDetails>()

    ////////////////////////ENERGY//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.ENERGY,
            points = nutriScoreDataDto?.energyPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.ENERGY,
                this.energyKcal100g
            ),
            contentPerHundredGram = this.energyKcal100g ?: 0,
            servingUnit = this.energyUnit ?: NutrientType.ENERGY.getDefaultServingUnit()
        )
    )

    ////////////////////////PROTEIN//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.SATURATES,
            points = nutriScoreDataDto?.saturatedFatPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.SATURATES,
                this.saturatedFat100g
            ),
            contentPerHundredGram = this.saturatedFat100g ?: 0,
            servingUnit = this.saturatedFatUnit ?: NutrientType.SATURATES.getDefaultServingUnit()
        )
    )


    ////////////////////////SATURATES//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.SATURATES,
            points = nutriScoreDataDto?.saturatedFatPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.SATURATES,
                this.saturatedFat100g
            ),
            contentPerHundredGram = this.saturatedFat100g ?: 0,
            servingUnit = this.saturatedFatUnit ?: NutrientType.SATURATES.getDefaultServingUnit()
        )
    )

    ///////////////////////SUGAR//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.SUGAR,
            points = nutriScoreDataDto?.sugarsPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.SUGAR,
                this.sugars100g
            ),
            contentPerHundredGram = this.sugars100g ?: 0 ,
            servingUnit = this.sugarsUnit ?: NutrientType.SUGAR.getDefaultServingUnit()
        )
    )

    ////////////////////////FIBRE//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.FIBRE,
            points = nutriScoreDataDto?.fiberPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.FIBRE,
                this.fiber100g
            ),
            contentPerHundredGram = this.fiber100g ?: 0,
            servingUnit = this.fiberUnit ?: NutrientType.FIBRE.getDefaultServingUnit()
        )
    )

    ////////////////////////SODIUM//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.SODIUM,
            points = nutriScoreDataDto?.sodiumPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.SODIUM,
                this.sodium100g
            ),
            contentPerHundredGram = this.sodium100g ?: 0,
            servingUnit = this.sodiumUnit ?: NutrientType.SODIUM.getDefaultServingUnit()
        )
    )
    ////////////////////////FRUITS, VEGETABLES AND NUTS//////////////////////////////////
    nutrients.add(
        NutrientDetails.fromDtoAttributes(
            nutrientType = NutrientType.FRUITS_VEGETABLES_AND_NUTS,
            points = nutriScoreDataDto?.fruitsVegetablesNutsColzaWalnutOliveOilsPoints ?: NutriScoreCalculator.getPoints(
                NutrientType.FRUITS_VEGETABLES_AND_NUTS,
                this.fruitsVegetablesNutsEstimateFromIngredients100g
            ),
            //TODO: Round content values to 2 decimals
            contentPerHundredGram = this.fruitsVegetablesNutsEstimateFromIngredients100g ?: 0,
            servingUnit = NutrientType.FRUITS_VEGETABLES_AND_NUTS.getDefaultServingUnit()
        )
    )

    return nutrients
}

fun NutrientType.getNutriScoreAndPointsCategory(points : Int) : Pair<PointsLevel, HealthCategory>{
    return when(this){
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

private fun NutrientType.getNutrientCategory(points: Int): NutrientCategory {
    return when(this){
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



private fun NutrientType.getDefaultServingUnit(): String{
    return when(this){
        NutrientType.ENERGY -> "Kcal"
        NutrientType.PROTEIN -> "g"
        NutrientType.SATURATES -> "g"
        NutrientType.SUGAR -> "g"
        NutrientType.FIBRE -> "g"
        NutrientType.SODIUM -> "g"
        NutrientType.FRUITS_VEGETABLES_AND_NUTS -> "%"
    }
}