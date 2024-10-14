package com.mdev.feature_profile.domain.model

import com.mdev.openfoodfacts_client.domain.model.NutrientPreference

sealed class ProfileDetailsListItems {
    data class UserName(val userName: String) : ProfileDetailsListItems()
    data class ContentHeader(val heading: String): ProfileDetailsListItems()
    data class DietaryPreferences(val nutrientPreference: NutrientPreference) :
        ProfileDetailsListItems()
}