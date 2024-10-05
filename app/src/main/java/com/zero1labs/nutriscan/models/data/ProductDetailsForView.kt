package com.zero1labs.nutriscan.models.data

import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.Nutrient

data class ProductDetailsForView(
    val mainDetailsForView: MainDetailsForView,
    val nutrient: Nutrient,
    val additive: List<Additive>
)
