package com.zero1labs.nutriscan.utils

enum class DietaryRestriction(val heading: String,val response: String,val conclusionString: String) {
    VEGAN("Vegan","en:vegan", "vegan"),
    NON_VEGAN("Non-Vegan","en:non-vegan", "vegan"),
    VEGAN_STATUS_UNKNOWN("Vegan Status Unknown","en:vegan-status-unknown", "vegan status is unknown"),
    PALM_OIL("Palm Oil","en:palm-oil", "contains palm oil"),
    PALM_OIL_FREE("Palm Oil Free","en:palm-oil-free", "palm oil free"),
    PALM_OIL_STATUS_UNKNOWN("Palm Oil Status Unknown","en:palm-oil-content-unknown", "palm oil content is unknown"),
    VEGETARIAN("Vegetarian","en:vegetarian", "vegetarian"),
    NON_VEGETARIAN("Non-Vegetarian","en:non-vegetarian", "non vegetarian"),
    VEGETARIAN_STATUS_UNKNOWN("Vegetarian Status Unknown","en:vegetarian-status-unknown", "vegetarian status is unknown")

}