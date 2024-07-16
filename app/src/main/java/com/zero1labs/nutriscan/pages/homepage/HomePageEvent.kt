package com.zero1labs.nutriscan.pages.homepage

sealed class HomePageEvent {
    data object SignOut: HomePageEvent()
    data class FetchProductDetails(val productId: String): HomePageEvent()
    data object AddItemToHistory: HomePageEvent()
    data object UpdateUserDetails: HomePageEvent()
}