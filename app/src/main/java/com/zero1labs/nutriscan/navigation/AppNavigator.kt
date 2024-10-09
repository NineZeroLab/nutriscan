package com.zero1labs.nutriscan.navigation

import androidx.navigation.NavController
import com.mdev.feature_register.navigation.RegisterNavigator
import com.zero1labs.nutriscan.R
import javax.inject.Inject

class AppNavigator @Inject constructor(
    private val navController: NavController
): RegisterNavigator {
    override fun navigateToLoginPage() {
        navController.navigate(R.id.action_navigate_to_login_page)
    }
}