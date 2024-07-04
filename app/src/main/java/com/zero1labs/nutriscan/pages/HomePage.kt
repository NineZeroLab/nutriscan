package com.zero1labs.nutriscan.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel
import com.zero1labs.nutriscan.viewModels.InternetConnectionState
import com.zero1labs.nutriscan.viewModels.ProductScanState
import kotlinx.coroutines.launch

class HomePage : Fragment(R.layout.fragment_home_page) {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarBg : View
    private lateinit var rvSearchHistoryItems : RecyclerView
    private lateinit var fabScanProduct : FloatingActionButton
    private lateinit var fabGetDemoItem: FloatingActionButton
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        progressBar = view.findViewById(R.id.progressBar)
        progressBarBg = view.findViewById(R.id.blurBg)
        rvSearchHistoryItems = view.findViewById(R.id.rv_search_history)
        fabScanProduct = view.findViewById(R.id.fab_scan_product)
        fabGetDemoItem = view.findViewById(R.id.fab_get_demo_item)

        val state = viewModel.uiState.value

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.internetConnectionState){
                    InternetConnectionState.Online -> {}
                    InternetConnectionState.Offline -> findNavController().navigate(R.id.action_home_page_to_no_internet_connection_page)
                    InternetConnectionState.Unchecked -> {}
                }
                when(state.productScanState){
                    ProductScanState.Success -> {
                        hideProgressBar()
                        Log.d("logger","Product fetch success")
                        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                            findNavController().navigate(R.id.action_home_page_to_product_details_page)
                    }

                    ProductScanState.Failure ->{
                        hideProgressBar()
                        Log.d("logger","Product fetch failure")
                        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                            findNavController().navigate(R.id.action_home_page_to_product_fetch_error_page)
                    }

                    ProductScanState.Loading -> {

                        Log.d("logger","Loading Product Details...")
                        showProgressBar()
                    }

                    ProductScanState.NotStarted -> Log.d("logger" , "product Scan not started")
                }
            }
        }

        fabScanProduct.setOnClickListener(){

            Log.d("logger", "starting gms barcode scanner")

            val scanner = GmsBarcodeScanning.getClient(requireContext(), BarCodeScannerOptions.options)
            scanner.startScan()
            .addOnSuccessListener { barcode ->
                Log.d("logger", "product scan successfully")
                viewModel.onEvent(AppEvent.OnStartScan(barcode.rawValue.toString()))
            }
            .addOnCanceledListener {

                Log.d("logger", "action cancelled by user")
            }
            .addOnFailureListener {
                Log.d("logger", it.message.toString())

            }
        }

        fabGetDemoItem.setOnClickListener{
            Log.d("logger", "Get Demo Item fab clicked")
            viewModel.onEvent(AppEvent.OnStartScan(productId = AppResources.getRandomItem()))

        }

        val searchHistoryItems : List<SearchHistoryListItem> = viewModel.uiState.value.searchHistory
        rvSearchHistoryItems.layoutManager = LinearLayoutManager(requireContext())
        rvSearchHistoryItems.adapter = SearchHistoryAdapter(searchHistoryItems)
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        progressBarBg.visibility = View.VISIBLE
        fabScanProduct.isClickable = false
        fabGetDemoItem.isClickable = false

    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
        progressBarBg.visibility = View.GONE
    }


}