package com.mdev.feature_scan.domain.model

import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.ProductDetails
import com.mdev.openfoodfacts_client.domain.model.getPalmOilStatus
import com.mdev.openfoodfacts_client.domain.model.getVeganStatus
import com.mdev.openfoodfacts_client.domain.model.getVegetarianStatus

internal data class ScanItemForView(
    val id: String,
    val name: String,
    val brand: String?,
    val healthCategory: HealthCategory,
    val imageUrl: String,
    val palmOilStatus: DietaryRestriction,
    val veganStatus: DietaryRestriction,
    val vegetarianStatus: DietaryRestriction,
    val additiveCount: Int,
    val allergenCount: Int
)

internal fun ProductDetails.toScanItemForView(): ScanItemForView{
    val healthCategory = getHealthCategory(this.nutriScoreGrade)
    val palmOilStatus = this.ingredientsAnalysis.getPalmOilStatus()
    val veganStatus = this.ingredientsAnalysis.getVeganStatus()
    val vegetarianStatus = this.ingredientsAnalysis.getVegetarianStatus()
    val additiveCount = this.additivesOriginalTags.size
    val allergenCount = this.allergensHierarchy.size
    return ScanItemForView(
        id = this.id,
        name = this.name,
        brand = this.brand,
        healthCategory = healthCategory,
        imageUrl = this.imageUrl,
        palmOilStatus = palmOilStatus,
        veganStatus = veganStatus,
        vegetarianStatus = vegetarianStatus,
        additiveCount = additiveCount,
        allergenCount = allergenCount
    )
}

private fun getHealthCategory(nutriScoreGrade: String?) : HealthCategory {
    return when(nutriScoreGrade){
        "a" ,
        "b" -> HealthCategory.GOOD
        "c" -> HealthCategory.FAIR
        "d" ,
        "e" -> HealthCategory.BAD
        else -> HealthCategory.UNKNOWN
    }
}