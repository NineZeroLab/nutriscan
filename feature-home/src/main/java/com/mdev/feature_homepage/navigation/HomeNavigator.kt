package com.mdev.feature_homepage.navigation

import androidx.fragment.app.Fragment

interface HomeNavigator {
    fun navigateToProductDetailsPage(fromFragment: Fragment, productId: String)
}