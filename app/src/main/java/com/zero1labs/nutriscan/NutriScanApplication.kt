package com.zero1labs.nutriscan

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NutriScanApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}