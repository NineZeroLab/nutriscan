package com.mdev.feature_scan.presentation.scan_page

internal sealed class ScanPageEvent {
    data class GetProductDetails(val productId: String): ScanPageEvent()
}