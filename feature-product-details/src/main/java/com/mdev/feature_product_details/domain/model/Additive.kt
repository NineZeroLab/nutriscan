package com.mdev.feature_product_details.domain.model
import com.mdev.common.R as CommonRes

internal data class Additive(
    val name: String,
    val riskLevel: AdditiveRiskLevel,
)

//Should be moved to common
enum class AdditiveRiskLevel(val displayText: String, val icon: Int){
    HAZARDOUS("Hazardous", CommonRes.drawable.circle_bad),
    MODERATE_RISK("Moderate Risk", CommonRes.drawable.circle_moderate),
    LIMITED_RISK("Limited Risk", CommonRes.drawable.circle_unknown),
    NO_RISK("No Risk", CommonRes.drawable.circle_good)
}

internal data class AdditivesShortView(
    val additiveRiskLevel: AdditiveRiskLevel,
    val count: Int
)


internal fun getDemoAdditivesShortView(): List<AdditivesShortView>{
    val additivesShortView = mutableListOf<AdditivesShortView>()
    AdditiveRiskLevel.entries.forEach { level ->
        additivesShortView.add(
            AdditivesShortView(
                additiveRiskLevel = level,
                count = additivesShortView.size
            )
        )
    }
    return additivesShortView
}