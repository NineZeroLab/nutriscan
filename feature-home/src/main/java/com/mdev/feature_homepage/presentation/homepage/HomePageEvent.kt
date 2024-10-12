package com.mdev.feature_homepage.presentation.homepage

sealed class HomePageEvent {
    data object LogOut: HomePageEvent()

}
