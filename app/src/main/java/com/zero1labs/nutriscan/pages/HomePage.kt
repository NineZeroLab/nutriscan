package com.zero1labs.nutriscan.pages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel
import com.zero1labs.nutriscan.viewModels.ProductScanState
import kotlinx.coroutines.launch

class HomePage : Fragment(R.layout.fragment_home_page) {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarBg : View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        progressBar = view.findViewById(R.id.progressBar)
        progressBarBg = view.findViewById(R.id.blurBg)
        lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.productScanState){

                    ProductScanState.Success -> {
                        hideProgressBar()
                        Log.d("logger","Product fetch success")
                        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                        if (state.shouldNavigate){
                            findNavController().navigate(R.id.action_home_page_to_product_details_page)
                            viewModel.setNavigation(false)
                        }
                    }

                    ProductScanState.Failure ->{
                        hideProgressBar()
                        Log.d("logger","Product fetch failure")
                        view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

                        if (state.shouldNavigate){
                            findNavController().navigate(R.id.action_home_page_to_product_fetch_error_page)
                            viewModel.setNavigation(false)
                        }
                    }

                    ProductScanState.Loading -> {

                        Log.d("logger","Loading Product Details...")
                        showProgressBar()
                    }

                    ProductScanState.NotStarted -> Log.d("logger" , "product Scan not started")
                }
            }
        }

        view.findViewById<Button>(R.id.btn_scan).setOnClickListener(){

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



    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        progressBarBg.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        progressBar.visibility = View.GONE
        progressBarBg.visibility = View.GONE
    }


}