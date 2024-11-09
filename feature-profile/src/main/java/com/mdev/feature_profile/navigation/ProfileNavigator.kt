package com.mdev.feature_profile.navigation

import androidx.fragment.app.Fragment

interface ProfileNavigator {
    fun navigateFromProfileToHomePage(fromFragment: Fragment)
}