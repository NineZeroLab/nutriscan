package com.mdev.feature_scan.presentation.scan_page

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_scan.databinding.FragmentScanPageBinding
import com.mdev.feature_scan.domain.model.ProductDetailsForView
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class ScanPage : Fragment() {
    private lateinit var viewBinding: FragmentScanPageBinding
    private lateinit var viewModel : ScanPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ScanPageViewModel::class.java]
        viewBinding = FragmentScanPageBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        buildScanList()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ state ->
                    when(state.scanState){
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            updateScanList(state.scannedProducts.last())
                        }
                        Status.FAILURE -> {}
                        Status.IDLE -> {}
                    }
                }
            }
        }
    }
    private fun buildScanList(){
        viewBinding.rvScanList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ScanListAdapter(emptyList())
        }
    }
    private fun updateScanList(item: ProductDetailsForView){
        val adapter = viewBinding.rvScanList.adapter as ScanListAdapter
        adapter.addItemToList(item)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_EAN_13
            )
            .build()


        //mutable map to keep track of barcode and the number of frames they show up
        val products = mutableMapOf<String,Int>()
        //adjust this value optimal for accuracy and speed
        val framesCount = 60

        val barcodeScanner = BarcodeScanning.getClient(options)
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
                        .addOnSuccessListener { barcodes ->
                            barcodes.getOrNull(0)?.rawValue?.let { code ->
                                if (products.containsKey(code)){
                                    products[code] = products.getValue(code) + 1
                                    logger("$code : $${products[code]}")
                                }else{
                                    products[code] = 1
                                    logger("$code : $${products[code]}")
                                }
                                if (products.getValue(code) == framesCount){
                                    viewModel.onEvent(ScanPageEvent.GetProductDetails(productId = code))
                                }
                            }
                        }
                        .addOnCompleteListener{
                         imageProxy.close()
                        }
                } ?: imageProxy.close()
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