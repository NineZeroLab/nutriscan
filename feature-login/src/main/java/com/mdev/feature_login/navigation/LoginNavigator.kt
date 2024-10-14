package com.mdev.feature_login.navigation

import androidx.fragment.app.Fragment

interface LoginNavigator {
    fun navigateToRegisterPage(fromFragment: Fragment)
    fun navigateFromLoginPageToHomePage(fromFragment: Fragment)
}