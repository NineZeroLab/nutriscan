package com.mdev.feature_product_details.ocr

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions

internal class BarCodeScannerOptions{

    companion object {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_UPC_A
            )
            .enableAutoZoom()
            .build()
    }
}