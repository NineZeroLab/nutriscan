package com.zero1labs.nutriscan.pages.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.AppResources.TAG
import kotlinx.coroutines.launch

class HomePage : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarBg : View
    private lateinit var rvSearchHistoryItems : RecyclerView
    private lateinit var fabScanProduct : FloatingActionButton
    private lateinit var fabGetDemoItem: FloatingActionButton
    private lateinit var tvUserName: TextView
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var viewModel: HomePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        return inflater.inflate(R.layout.fragment_home_page,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_bar)
        progressBarBg = view.findViewById(R.id.blurBg)
        rvSearchHistoryItems = view.findViewById(R.id.rv_search_history)
        fabScanProduct = view.findViewById(R.id.fab_scan_product)
        fabGetDemoItem = view.findViewById(R.id.fab_get_demo_item)
        val appCompatActivity: AppCompatActivity = activity as AppCompatActivity
        materialToolbar = appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)
        viewModel.onEvent(HomePageEvent.UpdateUserDetails)

        viewModel.uiState.value.appUser.let { user ->
            if (user != null){
                materialToolbar.title = "Hi ${user.name}"
            }else{
                materialToolbar.title = "Hi User"
            }
        }
        Log.d(TAG,"Products from firebase: ${viewModel.uiState.value.searchHistory}")

        requireActivity().addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.homepage_menu,menu)
                val appUser = viewModel.uiState.value.appUser
                menu.findItem(R.id.mi_sign_out)?.isVisible = appUser != null
                menu.findItem(R.id.mi_sign_in)?.isVisible = appUser == null
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.mi_sign_out -> {
                        viewModel.onEvent(HomePageEvent.SignOut)
                        findNavController().navigate(R.id.action_home_page_to_sign_in_page)
                        true
                    }
                    R.id.mi_sign_in -> {
                        findNavController().navigate(R.id.action_home_page_to_sign_in_page)
                        true
                    }
                    else -> {false}
                }
            }
        },viewLifecycleOwner)

        fabScanProduct.setOnClickListener(){

            Log.d("logger", "starting gms barcode scanner")

            val scanner = GmsBarcodeScanning.getClient(requireContext(), BarCodeScannerOptions.options)
            scanner.startScan()
            .addOnSuccessListener { barcode ->
                Log.d("logger", "product scan successfully")
                viewModel.onEvent(HomePageEvent.FetchProductDetails(barcode.rawValue.toString()))
            }
            .addOnCanceledListener {
                Log.d("logger", "action cancelled by user")
            }
            .addOnFailureListener {
                Log.d("logger", it.message.toString())
            }
        }

        fabGetDemoItem.setOnClickListener{
            Log.d("logger", "Get Demo Item fab clicked")
            viewModel.onEvent(HomePageEvent.FetchProductDetails(AppResources.getRandomItem()))

        }
        val searchHistoryItems : List<SearchHistoryListItem> = viewModel.uiState.value.searchHistory
        val searchHistoryAdapter = SearchHistoryAdapter(viewModel,searchHistoryItems)
        rvSearchHistoryItems.layoutManager = LinearLayoutManager(requireContext())
        rvSearchHistoryItems.adapter = searchHistoryAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.productScanState){
                    ProductScanState.Success -> {
                        hideProgressBar()
                        Log.d("logger","Product fetch success")
                        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                        findNavController().navigate(R.id.action_home_page_to_product_details_page)
                    }
                    ProductScanState.Failure ->{
                        hideProgressBar()
                        Log.d("logger","Product fetch failure")
                        view.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                        findNavController().navigate(R.id.action_home_page_to_error_page)
                    }
                    ProductScanState.Loading -> {

                        Log.d("logger","Loading Product Details...")
                        showProgressBar()
                    }
                    ProductScanState.NotStarted -> Log.d("logger" , "product Scan not started")
                }

                when(state.firebaseDataFetchState){
                    FirebaseDataFetchState.Loading -> {
                        showProgressBar()
                    }
                    FirebaseDataFetchState.Success -> {
                        hideProgressBar()
                        searchHistoryAdapter.updateData(state.searchHistory)
                    }
                    FirebaseDataFetchState.Failure -> {}
                    FirebaseDataFetchState.NotStarted -> {hideProgressBar()}
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.homepage_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        progressBarBg.visibility = View.VISIBLE
        fabScanProduct.isClickable = false
        fabGetDemoItem.isClickable = false

    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
        progressBarBg.visibility = View.GONE
    }


}