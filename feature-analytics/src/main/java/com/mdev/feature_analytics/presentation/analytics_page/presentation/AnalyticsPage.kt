package com.mdev.feature_analytics.presentation.analytics_page.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.mdev.client_firebase.data.remote.dto.AnalyticsData
import com.mdev.core.utils.hide
import com.mdev.core.utils.round
import com.mdev.core.utils.show
import com.mdev.feature_analytics.R
import com.mdev.feature_analytics.databinding.FragmentAnalyticsPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticsPage : Fragment(){
    private lateinit var viewBinding: FragmentAnalyticsPageBinding
    private lateinit var viewModel: AnalyticsPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAnalyticsPageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AnalyticsPageViewModel::class.java]
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ state ->
                    state.analyticsData?.let {
                        updateUi(it)
                    }
                }
            }
        }
    }

    private fun updateUi(analyticsData: AnalyticsData){
        if (analyticsData.scannedItems == 0){
            for (childView in viewBinding.llAnalyticsPageContent.children){
                childView.hide()
            }
            viewBinding.tvAnalyticsMessage.show()
            return
        }
        viewBinding.tvAnalyticsUsername.text = analyticsData.userName
        viewBinding.tvTotalScans.text = analyticsData.scannedItems.toString()
        viewBinding.tvAnalyticsGoodScanText.text = "Good Products "
        viewBinding.tvAnalyticsBadScanText.text  = "Bad Products  "
        viewBinding.tvAnalyticsGoodScanCount.text = analyticsData.goodProducts.toString()
        viewBinding.tvAnalyticsBadScanCount.text = analyticsData.badProducts.toString()

        viewBinding.rvAnalyticsCategory.apply {
            adapter = CategoryAdapter(analyticsData.topCategories.toList()
                .sortedByDescending { it.second }
                .take(4))
            layoutManager = GridLayoutManager(requireContext(),2)
        }

        viewBinding.llNutrientsAnalyticsData.removeAllViews()

        for((nutrient, averageValue) in analyticsData.averageNutrientPerProduct){
            if (nutrient == null) continue
            val nutrientView = LayoutInflater.from(requireContext()).inflate(R.layout.component_nutrient_analytics_data, viewBinding.llNutrientsAnalyticsData, false)

            nutrientView.findViewById<TextView>(R.id.tv_analytics_nutrient_name).text = nutrient.heading
            nutrientView.findViewById<TextView>(R.id.tv_analytics_nutrient_average).text = averageValue.round().toString()

            viewBinding.llNutrientsAnalyticsData.addView(nutrientView)
        }

    }
}