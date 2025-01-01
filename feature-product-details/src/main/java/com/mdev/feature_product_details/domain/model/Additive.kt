package com.mdev.feature_product_details.domain.model
import com.mdev.openfoodfacts_client.data.remote.dto.AdditiveDto
import com.mdev.common.R as CommonRes

//TODO: Should be moved to common
enum class AdditiveRiskLevel(val displayText: String, val icon: Int){
    HAZARDOUS("Hazardous", CommonRes.drawable.circle_bad),
    MODERATE_RISK("Moderate Risk", CommonRes.drawable.circle_moderate),
    LIMITED_RISK("Limited Risk", CommonRes.drawable.circle_moderate),
    NO_RISK("No Risk", CommonRes.drawable.circle_good),
    UNKNOWN("Unknown", CommonRes.drawable.circle_unknown)
}

internal data class AdditivesShortView(
    val name: String,
    val additiveRiskLevel: AdditiveRiskLevel,
)

internal fun getDemoAdditivesShortView(): List<AdditivesShortView>{
    val additivesShortView = mutableListOf<AdditivesShortView>()
    AdditiveRiskLevel.entries.forEach { level ->
        additivesShortView.add(
            AdditivesShortView(
                name = "Demo Additive",
                additiveRiskLevel = level,
            )
        )
    }
    return additivesShortView
}

internal fun AdditiveDto.toAdditiveShortView(): AdditivesShortView{
    return AdditivesShortView(
        name = this.name ?: "Unknown",
        additiveRiskLevel = AdditiveRiskLevel.UNKNOWN,
    )
}