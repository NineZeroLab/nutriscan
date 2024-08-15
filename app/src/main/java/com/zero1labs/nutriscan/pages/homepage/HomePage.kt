package com.zero1labs.nutriscan.pages.homepage

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.databinding.FragmentHomePageBinding
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.hide
import com.zero1labs.nutriscan.utils.invisible
import com.zero1labs.nutriscan.utils.logger
import com.zero1labs.nutriscan.utils.show
import kotlinx.coroutines.flow.collectLatest
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
            viewModel.uiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            )
                .collectLatest { state ->
                    when (state.productScanState) {
                        ProductScanState.Success -> {
//                            hideProgressBar()
                            logger("product Scan state: ${state.productScanState.name}")
                            logger("Showing progressBar")
//                            findNavController().navigate(R.id.action_home_page_to_product_details_page)
                        }

                        ProductScanState.Failure -> {
//                            hideProgressBar()
                            logger("Hiding progressBar")
                            logger("product Scan state: ${state.productScanState.name}")
                            Snackbar.make(view, state.msg.toString(), Snackbar.LENGTH_LONG).show()
    //                        findNavController().navigate(R.id.action_home_page_to_error_page)
                        }

                        ProductScanState.Loading -> {
                            logger("product Scan state: ${state.productScanState.name}")
                            findNavController().navigate(R.id.action_home_page_to_product_details_page)
//                            showProgressBar()
                        }

                        ProductScanState.NotStarted -> {
                            logger("product Scan state: ${state.productScanState.name}")                        }
                    }

                    when (state.firebaseDataFetchState) {
                        FirebaseDataFetchState.Loading -> {
                            showProgressBar()
                            logger("firebase data fetch state ${state.firebaseDataFetchState.name}")
                        }
                        FirebaseDataFetchState.Success -> {
                            logger("firebase data fetch state ${state.firebaseDataFetchState.name}")
                            hideProgressBar()
                            viewBinding.mtbHomepage.title = "Hi ${state.appUser?.name}"
                            (viewBinding.rvSearchHistory.adapter as SearchHistoryAdapter).updateData(
                                state.searchHistory
                            )
                        }

                        FirebaseDataFetchState.Failure -> {
                            logger("firebase data fetch state ${state.firebaseDataFetchState.name}")
                            hideProgressBar()
                        }

                        FirebaseDataFetchState.NotStarted -> {
                            logger("firebase data fetch state ${state.firebaseDataFetchState.name}")
                            if (state.appUser == null) {
                                logger("app user is null... navigating to sign in page")
                                findNavController().navigate(R.id.action_home_page_to_sign_in_page)
                            }
                        }
                    }
                }
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
            viewModel.onEvent(HomePageEvent.FetchProductDetails(AppResources.getRandomItem()))

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

        viewBinding.mtbHomepage.overflowIcon?.setTint(ContextCompat.getColor(requireContext(),R.color.md_theme_onPrimary))
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
                        findNavController().navigate(R.id.action_home_page_to_welcome_page)
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