package com.mdev.feature_scan

import androidx.fragment.app.Fragment

interface ScanNavigator {
    fun navigateToProductDetailsPage(fromFragment: Fragment, productId: String)
}