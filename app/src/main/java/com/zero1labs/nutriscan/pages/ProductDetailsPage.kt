package com.zero1labs.nutriscan.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zero1labs.nutriscan.NutrientsAdapter
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.utils.NutrientCategory
import com.zero1labs.nutriscan.data.models.NutrientGenerator
import com.zero1labs.nutriscan.data.models.ProductDetailsListItems
import com.zero1labs.nutriscan.viewModels.AppViewModel

class ProductDetailsPage : Fragment(R.layout.fragment_product_details_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val state = viewModel.uiState.value
        val productDetailsList = mutableListOf<ProductDetailsListItems>()
        state.product?.let {product ->
            val nutrientGenerator = NutrientGenerator(product)
            ProductDetailsListItems.ProductHeader(MainDetailsForView.getMainDetailsForView(product)).let {productHeader ->
                productDetailsList.add(element = productHeader)
            }

            if (nutrientGenerator.getNutrientsCount(NutrientCategory.NEGATIVE) > 0){

                ProductDetailsListItems.NutrientsHeaderForView(NutrientCategory.NEGATIVE).let { nutrientsHeader ->
                    productDetailsList.add(element = nutrientsHeader)
                }

                NutrientGenerator(product).generateNutrientsForView(NutrientCategory.NEGATIVE).forEach { nutrient ->
                    productDetailsList.add(element = ProductDetailsListItems.NegativeNutrientsForView(nutrient))
                }
            }

            if (nutrientGenerator.getNutrientsCount(NutrientCategory.POSITIVE) > 0){

                ProductDetailsListItems.NutrientsHeaderForView(NutrientCategory.POSITIVE).let { nutrientsHeader ->
                    productDetailsList.add(element = nutrientsHeader)
                }

                NutrientGenerator(product).generateNutrientsForView(NutrientCategory.POSITIVE).forEach{ nutrient ->
                    productDetailsList.add(element = ProductDetailsListItems.PositiveNutrientsForView(nutrient))
                }
            }



        }




        val recyclerView: RecyclerView = view.findViewById(R.id.rv_product_details)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NutrientsAdapter(productDetailsList)

    }
}
