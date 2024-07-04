package com.zero1labs.nutriscan.viewModels

import com.zero1labs.nutriscan.data.models.remote.Product

sealed class AppEvent {
    data class GetProductDetails(val product: Product) : AppEvent()
    data class OnStartScan(val productId : String) : AppEvent()
}