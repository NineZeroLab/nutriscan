package com.zero1labs.nutriscan.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mdev.core.utils.logger
import com.mdev.feature_homepage.navigation.HomeNavigator
import com.mdev.feature_login.navigation.LoginNavigator
import com.mdev.feature_product_details.navigation.ProductDetailsNavigator
import com.mdev.feature_profile.navigation.ProfileNavigator
import com.mdev.feature_register.navigation.RegisterNavigator
import com.zero1labs.nutriscan.R
import javax.inject.Inject

class AppNavigator @Inject constructor(): HomeNavigator, LoginNavigator, ProductDetailsNavigator, ProfileNavigator, RegisterNavigator  {
    override fun navigateToLoginPage(fromFragment: Fragment) {
        fromFragment.findNavController().navigate(R.id.action_navigate_to_login_page)
    }

    override fun navigateToProductDetailsPage(fromFragment: Fragment, productId: String) {
        val bundle = Bundle().apply {
            putString("productId", productId)
        }
        fromFragment.findNavController().navigate(R.id.action_navigate_to_product_details_page,bundle)
    }

    override fun navigateToRegisterPage(fromFragment: Fragment) {
        fromFragment.findNavController().navigate(R.id.action_navigate_to_register_page)

    }

    override fun navigateToHomePage(fromFragment: Fragment) {
        logger("Navigating to HomePage")
        fromFragment.findNavController().navigate(R.id.action_navigate_to_home_page)
    }

    override fun popBackStack(fromFragment: Fragment){
        fromFragment.findNavController().popBackStack()
    }

}