package com.mdev.feature_register.navigation

import androidx.fragment.app.Fragment

interface RegisterNavigator {
    fun navigateToLoginPage(fromFragment: Fragment)
    fun popBackStack(fromFragment: Fragment)
}