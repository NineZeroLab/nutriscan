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
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.databinding.FragmentHomePageBinding
import com.zero1labs.nutriscan.ocr.BarCodeScannerOptions
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.hide
import com.zero1labs.nutriscan.utils.logger
import com.zero1labs.nutriscan.utils.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomePage : Fragment() {

    private lateinit var viewModel: HomePageViewModel
    private lateinit var viewBinding: FragmentHomePageBinding
    private lateinit var materialToolbar: MaterialToolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        viewBinding = FragmentHomePageBinding.inflate(inflater, container, false)
        return  viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildToolbar()
        viewModel.onEvent(HomePageEvent.UpdateUserDetails)


        Log.d(TAG,"Products from firebase: ${viewModel.uiState.value.searchHistory}")

        handleScanButton()

        handleDemoItemButton()
        buildSearchHistoryRv()
        handleUiState(view)
    }

    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            )
                .collectLatest { state ->
                    when (state.productScanState) {
                        ProductScanState.Success -> {
                            hideProgressBar()
                            logger(state.productScanState.name)
                            findNavController().navigate(R.id.action_home_page_to_product_details_page)
                        }

                        ProductScanState.Failure -> {
                            hideProgressBar()
                            logger(state.productScanState.name)
                            Snackbar.make(view, state.msg.toString(), Snackbar.LENGTH_LONG).show()
    //                        findNavController().navigate(R.id.action_home_page_to_error_page)
                        }

                        ProductScanState.Loading -> {
                            logger(state.productScanState.name)
                            showProgressBar()
                        }

                        ProductScanState.NotStarted -> {
                            logger(state.productScanState.name)
                        }
                    }

                    when (state.firebaseDataFetchState) {
                        FirebaseDataFetchState.Loading -> {
                            showProgressBar()
                            logger(state.firebaseDataFetchState.name)
                        }
                        FirebaseDataFetchState.Success -> {
                            hideProgressBar()
                            materialToolbar.title = "Hi ${state.appUser?.name}"
                            logger(state.firebaseDataFetchState.name)
                            (viewBinding.rvSearchHistory.adapter as SearchHistoryAdapter).updateData(
                                state.searchHistory
                            )
                        }

                        FirebaseDataFetchState.Failure -> {
                            hideProgressBar()
                            logger(state.firebaseDataFetchState.name)
                        }

                        FirebaseDataFetchState.NotStarted -> {
                            logger(state.firebaseDataFetchState.name)
                            if (state.appUser == null) {
                                logger("app user is null... navigating to sign in page")
                                findNavController().navigate(R.id.action_home_page_to_sign_in_page)
                            }
                        }
                    }
                }
        }
    }

    private fun buildSearchHistoryRv() {
        val searchHistoryItems : List<SearchHistoryListItem> = viewModel.uiState.value.searchHistory
        val searchHistoryAdapter = SearchHistoryAdapter(viewModel,searchHistoryItems)
        viewBinding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvSearchHistory.adapter = searchHistoryAdapter
    }

    private fun handleDemoItemButton() {
        viewBinding.fabGetDemoItem.setOnClickListener {
            Log.d("logger", "Get Demo Item fab clicked")
            viewModel.onEvent(HomePageEvent.FetchProductDetails(AppResources.getRandomItem()))

        }
    }

    private fun handleScanButton() {
        viewBinding.fabScanProduct.setOnClickListener() {
            Log.d("logger", "starting gms barcode scanner")
            val scanner =
                GmsBarcodeScanning.getClient(requireContext(), BarCodeScannerOptions.options)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    logger("barcode scan success")
                    viewModel.onEvent(HomePageEvent.FetchProductDetails(barcode.rawValue.toString()))
                }
                .addOnCanceledListener {
                    logger("barcode scan cancelled")
                }
                .addOnFailureListener {
                    logger("barcode scan failure")
                }
        }
    }

    private fun buildToolbar() {
        val appCompatActivity: AppCompatActivity = activity as AppCompatActivity
        materialToolbar = appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)
        viewModel.uiState.value.appUser.let { user ->
            if (user != null){
                materialToolbar.title = "Hi ${user.name}"
                materialToolbar.overflowIcon?.setTint(ContextCompat.getColor(requireContext(),R.color.md_theme_onPrimary))
            }
        }

        requireActivity().addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.homepage_menu,menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.mi_sign_out -> {
                        viewModel.onEvent(HomePageEvent.SignOut)
                        true
                    }
                    R.id.mi_edit_profile -> {
                        findNavController().navigate(R.id.action_home_page_to_welcome_page)
                        true
                    }
                    else -> {false}
                }
            }
        },viewLifecycleOwner)

    }

    private fun showProgressBar(){
        viewBinding.apply {
            progressBar.show()
            blurBg.show()
            fabScanProduct.isClickable = false
            fabGetDemoItem.isClickable = false
        }
    }

    private fun hideProgressBar(){
        viewBinding.apply {
            progressBar.hide()
            blurBg.hide()
            fabScanProduct.isClickable = true
            fabGetDemoItem.isClickable = true
        }
    }


}