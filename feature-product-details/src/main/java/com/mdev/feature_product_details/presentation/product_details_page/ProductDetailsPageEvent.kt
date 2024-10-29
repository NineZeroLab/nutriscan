package com.mdev.feature_product_details.presentation.product_details_page

internal sealed class ProductDetailsPageEvent {
    internal data class GetProductDetails(val productId: String): ProductDetailsPageEvent()
}