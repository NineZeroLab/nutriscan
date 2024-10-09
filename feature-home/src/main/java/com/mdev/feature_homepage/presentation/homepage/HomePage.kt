package com.mdev.feature_homepage.presentation.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.mdev.openfoodfacts_client.utils.ClientResources.TAG
import com.mdev.core.utils.hide
import com.mdev.core.utils.invisible
import com.mdev.core.utils.logger
import com.mdev.core.utils.show
import com.mdev.feature_homepage.R
import com.mdev.common.R as CommonRes
import com.mdev.feature_homepage.databinding.FragmentHomePageBinding
import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import com.mdev.feature_homepage.ocr.BarCodeScannerOptions
import kotlinx.coroutines.launch

class HomePage : Fragment() {

    private lateinit var viewModel: HomePageViewModel
    private lateinit var viewBinding: FragmentHomePageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        viewBinding = FragmentHomePageBinding.inflate(inflater, container, false)
        return  viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(HomePageEvent.UpdateUserDetails)
        buildToolbar()
        Log.d(TAG,"Products from firebase: ${viewModel.uiState.value.searchHistory}")
        handleScanButton()
        handleDemoItemButton()
        buildSearchHistoryRv()
        handleUiState(view)
    }

    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {

        }
    }

    private fun buildSearchHistoryRv() {
        val searchHistoryItems : List<SearchHistoryListItem> = viewModel.uiState.value.searchHistory
        val searchHistoryAdapter = SearchHistoryAdapter(viewModel,searchHistoryItems)
        viewBinding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvSearchHistory.adapter = searchHistoryAdapter
    }

    private fun handleDemoItemButton() {
        viewBinding.fabGetDemoItem.setOnClickListener {
            Log.d("logger", "Get Demo Item fab clicked")
//            viewModel.onEvent(HomePageEvent.FetchProductDetails(ClientResources.getRandomItem()))
        }
    }

    private fun handleScanButton() {
        viewBinding.fabScanProduct.setOnClickListener() {
            Log.d("logger", "starting gms barcode scanner")
            val scanner =
                GmsBarcodeScanning.getClient(requireContext(), BarCodeScannerOptions.options)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    logger("barcode scan success")
                    viewModel.onEvent(HomePageEvent.FetchProductDetails(barcode.rawValue.toString()))
                }
                .addOnCanceledListener {
                    logger("barcode scan cancelled")
                }
                .addOnFailureListener {
                    logger("barcode scan failure")
                }
        }
    }

    private fun buildToolbar() {

        viewBinding.mtbHomepage.overflowIcon?.setTint(ContextCompat.getColor(requireContext(),CommonRes.color.md_theme_onPrimary))
        viewModel.uiState.value.appUser?.let { user ->
            viewBinding.mtbHomepage.apply {
                title = "Hi ${user.name}"
            }
        }
        viewBinding.mtbHomepage.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.homepage_menu,menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.mi_sign_out -> {
                        viewModel.onEvent(HomePageEvent.SignOut)
                        true
                    }
                    R.id.mi_edit_profile -> {
//                        findNavController().navigate(R.id.action_home_page_to_welcome_page)
                        true
                    }
                    else -> {false}
                }
            }
        },viewLifecycleOwner)
    }

    private fun showProgressBar(){
        logger("trying to show progress bar")
        viewBinding.homepageMainLayout.invisible()
        viewBinding.progressbarLayout.show()
    }

    private fun hideProgressBar(){
        logger("trying to hide progress bar")
        viewBinding.homepageMainLayout.show()
        viewBinding.progressbarLayout.hide()
    }


}