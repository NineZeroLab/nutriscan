package com.zero1labs.nutriscan.pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.utils.NutrientCategory
import com.zero1labs.nutriscan.data.models.NutrientGenerator
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.models.data.ProductDetailsListItems
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel
import com.zero1labs.nutriscan.viewModels.ProductScanState
import kotlinx.coroutines.launch

class ProductDetailsPage : Fragment(R.layout.fragment_product_details_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        val btnBackToHomePage : View = view.findViewById(R.id.btn_back_to_homepage)
        val btnScanAgain : View = view.findViewById(R.id.btn_scan_again)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.productScanState){
                    ProductScanState.Success -> {
                        Log.d("logger" , "scan status updated in viewModel")
                    }
                    ProductScanState.Failure -> {
                        Log.d("logger" , "product data fetching error in viewModel")
                        findNavController().navigate(R.id.action_productDetailsPageLayout_to_ProductFetchErrorPage)
                    }
                    ProductScanState.Loading -> {
                        Log.d("logger" , "loading product details in viewModel")
                    }
                    ProductScanState.NotStarted -> Log.d("logger", "product scan not started")
                }
            }
        }




        val state = viewModel.uiState

        viewLifecycleOwner.lifecycleScope.launch {
            state.collect(){state ->
                val productDetailsList = state.product?.let { getProductDetailsList(it) }
                val recyclerView: RecyclerView = view.findViewById(R.id.rv_product_details)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = productDetailsList?.let { NutrientsAdapter(it) }
            }
        }



        btnBackToHomePage.setOnClickListener{
            findNavController().popBackStack(findNavController().graph.startDestinationId,false)
        }

        btnScanAgain.setOnClickListener{
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


    private fun getProductDetailsList(product: Product) : List<ProductDetailsListItems>{
        val productDetailsList = mutableListOf<ProductDetailsListItems>()
        val nutrientGenerator = NutrientGenerator(product)
        ProductDetailsListItems.ProductHeader(MainDetailsForView.getMainDetailsForView(product)).let { productHeader ->
            productDetailsList.add(element = productHeader)
        }

        if (nutrientGenerator.getNutrientsCount(NutrientCategory.NEGATIVE) > 0){

            ProductDetailsListItems.NutrientsHeaderForView(NutrientCategory.NEGATIVE,AppResources.getProductType(product.categoriesHierarchy)).let { nutrientsHeader ->
                productDetailsList.add(element = nutrientsHeader)
            }

            NutrientGenerator(product).generateNutrientsForView(NutrientCategory.NEGATIVE).forEach { nutrient ->
                productDetailsList.add(element = ProductDetailsListItems.NegativeNutrientsForView(nutrient))
            }
        }

        if (nutrientGenerator.getNutrientsCount(NutrientCategory.POSITIVE) > 0){

            ProductDetailsListItems.NutrientsHeaderForView(NutrientCategory.POSITIVE,AppResources.getProductType(product.categoriesHierarchy)).let { nutrientsHeader ->
                productDetailsList.add(element = nutrientsHeader)
            }

            NutrientGenerator(product).generateNutrientsForView(NutrientCategory.POSITIVE).forEach{ nutrient ->
                productDetailsList.add(element = ProductDetailsListItems.PositiveNutrientsForView(nutrient))
            }
        }

        return productDetailsList

    }
}
