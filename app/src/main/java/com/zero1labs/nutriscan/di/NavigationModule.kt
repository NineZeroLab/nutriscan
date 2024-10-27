package com.zero1labs.nutriscan.di

import androidx.fragment.app.FragmentActivity
import com.mdev.feature_history.navigation.HistoryNavigator
import com.mdev.feature_homepage.navigation.HomeNavigator
import com.mdev.feature_login.navigation.LoginNavigator
import com.mdev.feature_product_details.navigation.ProductDetailsNavigator
import com.mdev.feature_profile.navigation.ProfileNavigator
import com.mdev.feature_register.navigation.RegisterNavigator
import com.zero1labs.nutriscan.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigationModule{

    @Binds
    abstract fun bindFragmentActivity(activity: FragmentActivity): FragmentActivity

    @Binds
    abstract fun bindHomePageNavigator(appNavigator: AppNavigator): HomeNavigator

    @Binds
    abstract fun bindProductDetailsNavigator(appNavigator: AppNavigator): ProductDetailsNavigator

    @Binds
    abstract fun bindLoginNavigator(appNavigator: AppNavigator): LoginNavigator

    @Binds
    abstract fun bindRegisterNavigator(appNavigator: AppNavigator): RegisterNavigator

    @Binds
    abstract fun bindProfileNavigator(appNavigator: AppNavigator): ProfileNavigator

    @Binds
    abstract fun bindHistoryNavigator(appNavigator: AppNavigator): HistoryNavigator
}


