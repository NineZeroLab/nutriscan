package com.zero1labs.nutriscan.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdev.core.utils.logger
import com.mdev.feature_history.navigation.HistoryNavigator
import com.mdev.feature_homepage.navigation.HomeNavigator
import com.mdev.feature_login.navigation.LoginNavigator
import com.mdev.feature_product_details.navigation.ProductDetailsNavigator
import com.mdev.feature_profile.navigation.ProfileNavigator
import com.mdev.feature_register.navigation.RegisterNavigator
import com.mdev.feature_scan.ScanNavigator
import com.zero1labs.nutriscan.R
import javax.inject.Inject

class AppNavigator @Inject constructor(): HomeNavigator, LoginNavigator, ProductDetailsNavigator, ProfileNavigator, RegisterNavigator, HistoryNavigator, ScanNavigator{
    override fun navigateToLoginPage(fromFragment: Fragment) {
        fromFragment.findNavController().navigate(R.id.action_navigate_to_login_page)
    }

    override fun navigateToProductDetailsPage(fromFragment: Fragment, productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        fromFragment.findNavController().navigate(R.id.action_navigate_to_product_details_page,bundle)
    }

    override fun navigateFromHomePageToLoginPage(fromFragment: Fragment) {
        //TODO: clean up this function
    }

    override fun navigateToRegisterPage(fromFragment: Fragment) {
        fromFragment.findNavController().navigate(R.id.action_navigate_to_register_page)

    }

    override fun navigateFromLoginPageToHomePage(fromFragment: Fragment) {
        logger("Navigating to HomePage")
        fromFragment.findNavController().navigate(R.id.action_login_page_to_home_page)
    }

    override fun popBackStack(fromFragment: Fragment){
        fromFragment.findNavController().popBackStack()
    }

    override fun navigateFromProfileToHomePage(fromFragment: Fragment) {
        logger("Navigating to HomePage")
        val bottomNavigationView = fromFragment.activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.selectedItemId = R.id.home_graph
    }

    override fun logout(fromFragment: Fragment) {
        fromFragment.findNavController().navigate(R.id.action_logout)
    }

    override fun navigateToHomePage(fromFragment: Fragment) {
        //TODO: for product details to homepage
    }

    override fun reloadWithNewProduct(fromFragment: Fragment, productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        val navOptions = NavOptions.Builder()
            .setPopUpTo(com.mdev.feature_product_details.R.id.product_details_page,true)
            .build()
        fromFragment.findNavController().navigate(com.mdev.feature_product_details.R.id.product_details_page, bundle, navOptions)
    }

}