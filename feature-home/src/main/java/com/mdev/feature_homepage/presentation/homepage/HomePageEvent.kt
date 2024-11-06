package com.mdev.feature_homepage.presentation.homepage

sealed class HomePageEvent {
    data object getSearchHistory: HomePageEvent()
    data object getRecommendedProducts: HomePageEvent()
    data object getUserDetails: HomePageEvent()

}
