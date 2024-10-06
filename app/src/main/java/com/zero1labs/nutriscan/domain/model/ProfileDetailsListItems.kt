package com.zero1labs.nutriscan.domain.model

import com.mdev.core.domain.model.NutrientPreference

sealed class ProfileDetailsListItems {
    data class UserName(val userName: String) : ProfileDetailsListItems()
    data class ContentHeader(val heading: String): ProfileDetailsListItems()
    data class DietaryPreferences(val nutrientPreference: NutrientPreference) :
        ProfileDetailsListItems()
}