package com.mdev.feature_history.navigation

import androidx.fragment.app.Fragment

interface HistoryNavigator {
    fun navigateToProductDetailsPage(fromFragment: Fragment, productId:String)
}