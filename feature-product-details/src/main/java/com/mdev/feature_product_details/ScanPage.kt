package com.mdev.feature_product_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.mdev.feature_product_details.databinding.FragmentScanPageBinding
import com.mdev.feature_product_details.navigation.ProductDetailsNavigator
import com.mdev.feature_product_details.ocr.BarCodeScannerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScanPage : Fragment() {

    private lateinit var viewBinding: FragmentScanPageBinding
    @Inject
    lateinit var navigator: ProductDetailsNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentScanPageBinding.inflate(inflater,container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildScanner()

    }

    private fun buildScanner() {
        val scanner = GmsBarcodeScanning.getClient(requireContext(), BarCodeScannerOptions.options)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val bundle = Bundle().apply {
                    putString("barcode", barcode.rawValue)
                }
                findNavController().navigate(R.id.action_scan_page_to_product_details_page, bundle)
            }.addOnFailureListener {
                //navigate back To HomePage
            }
    }


}