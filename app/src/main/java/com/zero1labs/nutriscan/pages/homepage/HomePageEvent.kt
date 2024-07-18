package com.zero1labs.nutriscan.pages.homepage

import com.zero1labs.nutriscan.models.data.AppUser

sealed class HomePageEvent {
    data object SignOut: HomePageEvent()
    data class FetchProductDetails(val productId: String): HomePageEvent()
    data object AddItemToHistory: HomePageEvent()
    data object UpdateUserDetails: HomePageEvent()
    data class UpdateUserPreferences(val appUser: AppUser):HomePageEvent()
}