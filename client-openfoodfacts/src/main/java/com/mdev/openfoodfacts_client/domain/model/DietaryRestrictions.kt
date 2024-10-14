package com.mdev.openfoodfacts_client.domain.model

import android.util.Log
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction.*
import com.mdev.openfoodfacts_client.utils.ClientResources.TAG

enum class DietaryRestriction(val heading: String,val response: String,val conclusionString: String) {
    VEGAN("Vegan","en:vegan", "Vegan"),
    NON_VEGAN("Non-Vegan","en:non-vegan", "Non-Vegan"),
    VEGAN_STATUS_UNKNOWN("Vegan Status Unknown","en:vegan-status-unknown", "Vegan status is unknown"),
    PALM_OIL("Palm Oil","en:palm-oil", "contains Palm Oil"),
    PALM_OIL_FREE("Palm Oil Free","en:palm-oil-free", "Palm Oil free"),
    PALM_OIL_STATUS_UNKNOWN("Palm Oil Status Unknown","en:palm-oil-content-unknown", "Palm Oil content is unknown"),
    VEGETARIAN("Vegetarian","en:vegetarian", "Vegetarian"),
    NON_VEGETARIAN("Non-Vegetarian","en:non-vegetarian", "Non-Vegetarian"),
    VEGETARIAN_STATUS_UNKNOWN("Vegetarian Status Unknown","en:vegetarian-status-unknown", "Vegetarian status is unknown")
}


fun getDietaryRestrictions(ingredientsHierarchy: List<String>?): List<DietaryRestriction>{
    Log.d(TAG,"restrictions: ${ingredientsHierarchy.toString()}")
    val restrictionsList = mutableListOf<DietaryRestriction>()
    DietaryRestriction.entries.forEach { restriction ->
        if (ingredientsHierarchy?.contains(restriction.response) == true){
            restrictionsList.add(restriction)
        }
    }
    return restrictionsList
}


fun getDietaryRestrictionConclusion(productRestrictions: List<DietaryRestriction>, userRestrictions: List<DietaryRestriction>?): String{
    if (productRestrictions.isEmpty()){
        return "Not enough data for calculating restrictions"
    }
    val veganRestrictions = listOf(
        VEGAN,
        NON_VEGAN,
        VEGAN_STATUS_UNKNOWN
    )
    val palmOilRestrictions = listOf(
        PALM_OIL,
        PALM_OIL_FREE,
        PALM_OIL_STATUS_UNKNOWN
    )
    val vegetarianRestrictions = listOf(
        VEGETARIAN,
        NON_VEGETARIAN,
        VEGETARIAN_STATUS_UNKNOWN
    )
    val matchedRestrictions = mutableListOf<DietaryRestriction>()
    productRestrictions.forEach {productRestriction ->
        userRestrictions?.forEach { userRestriction ->
            if (userRestriction == VEGAN && productRestriction in veganRestrictions){
                matchedRestrictions.add(productRestriction)
            }else if (userRestriction == VEGETARIAN && productRestriction in vegetarianRestrictions){
                matchedRestrictions.add(productRestriction)
            }else if (userRestriction == PALM_OIL_FREE && productRestriction in palmOilRestrictions){
                matchedRestrictions.add(productRestriction)
            }
        }
    }
    return matchedRestrictions.joinToString(", "){ restriction ->
        restriction.conclusionString
    }
}
