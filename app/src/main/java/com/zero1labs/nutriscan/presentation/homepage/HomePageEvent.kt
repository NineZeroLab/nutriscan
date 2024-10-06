package com.zero1labs.nutriscan.presentation.homepage

import com.mdev.client_firebase.data.remote.dto.AppUser

sealed class HomePageEvent {
    data object SignOut: HomePageEvent()
    data class FetchProductDetails(val productId: String): HomePageEvent()
    data object AddItemToHistory: HomePageEvent()
    data object UpdateUserDetails: HomePageEvent()
    data class UpdateUserPreferences(val appUser: AppUser):HomePageEvent()
}