package com.zero1labs.nutriscan.pages

import android.os.Bundle
import android.provider.MediaStore.Images.ImageColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.viewModels.AppViewModel

class ProductDetailsPage : Fragment(R.layout.fragment_product_details_page) {
    private lateinit var tvProductName : TextView
    private lateinit var tvProductBrand : TextView
    private lateinit var ivProductImage : ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvProductBrand = view.findViewById(R.id.tvproductBrand)
        tvProductName = view.findViewById(R.id.tvProductName)
        ivProductImage = view.findViewById(R.id.ivProductImage)

        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val state = viewModel.uiState.value


        tvProductName.text = state.product?.productName
        tvProductBrand.text = state.product?.brand
        Glide.with(this)
            .load(state.product?.imageUrl)
            .into(ivProductImage)

    }
}
