package com.mdev.feature_homepage.ocr

import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.Barcode.BarcodeFormat
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions

class BarCodeScannerOptions{

    companion object {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_UPC_A
            )
            .enableAutoZoom()
            .build()
    }
}