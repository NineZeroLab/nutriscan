package com.mdev.feature_scan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.mdev.feature_scan.databinding.FragmentScanPageBinding
import java.util.concurrent.Executors

class ScanPage : Fragment() {
    private lateinit var viewBinding: FragmentScanPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentScanPageBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val barcodeScanner = BarcodeScanning.getClient()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewBinding.pvCamera.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()){ imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val inputImage = imageProxy.image?.let {
                    InputImage.fromMediaImage(it, rotationDegrees)
                }
                inputImage?.let {
                    barcodeScanner.process(it)
                        .addOnSuccessListener { barcode ->
                            Log.d("logger",barcode[0].rawValue.toString())
                        }
                }
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            }catch (e: Exception){
                Log.d("logger", "camera x usecase binding failed")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

}