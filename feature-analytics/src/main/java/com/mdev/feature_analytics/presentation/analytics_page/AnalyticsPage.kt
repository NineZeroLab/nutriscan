package com.mdev.feature_analytics.presentation.analytics_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mdev.feature_analytics.databinding.FragmentAnalyticsPageBinding

class AnalyticsPage : Fragment(){
    private lateinit var viewBinding: FragmentAnalyticsPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAnalyticsPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}