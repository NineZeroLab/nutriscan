package com.mdev.feature_product_details.presentation.profilePage

internal sealed class ProductDetailsPageEvent {
    internal data class GetProductDetails(val productId: String): ProductDetailsPageEvent()
}