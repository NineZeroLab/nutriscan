package com.mdev.feature_homepage.presentation.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.hide
import com.mdev.core.utils.invisible
import com.mdev.core.utils.logger
import com.mdev.core.utils.show
import com.mdev.feature_homepage.R
import com.mdev.common.R as CommonRes
import com.mdev.feature_homepage.databinding.FragmentHomePageBinding
import com.mdev.feature_homepage.domain.model.RecommendedProduct
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.domain.model.getDummyHistoryitem
import com.mdev.feature_homepage.domain.model.getDummyRecommendedProducts
import com.mdev.feature_homepage.navigation.HomeNavigator
import com.mdev.openfoodfacts_client.utils.ClientResources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomePage : Fragment() {
    private lateinit var viewBinding: FragmentHomePageBinding
    private lateinit var viewModel: HomePageViewModel
    @Inject
    lateinit var navigator: HomeNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        viewBinding = FragmentHomePageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        return  viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildSearchHistoryRecyclerView()
        buildRecommendedProductsRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ state ->
                    when(state.recommendedProductFetchState){
                        Status.LOADING -> {
                            logger("loading recommended products")
                        }
                        Status.SUCCESS -> {
                            logger("success fetching recommended products")
                            updateRecommendedProducts()
                        }
                        Status.FAILURE -> {
                            logger("failure fetching recommended products")

                        }
                        Status.IDLE -> {

                        }
                    }
                    when(state.appUserDataFetchState){
                        Status.LOADING -> {
                            logger("Loading user details...")
                        }
                        Status.SUCCESS -> {
                            logger("got user details!!!")
                            viewBinding.tvHomepageUsername.text = "Hello, ${state.appUser?.name}"
                        }
                        Status.FAILURE -> {
                            logger("error fetching user details!!!")
                        }
                        Status.IDLE -> {

                        }
                    }


                    when(state.searchHistoryFetchState){
                        Status.LOADING -> {
                            logger("Loading Search History ....")
                        }
                        Status.SUCCESS -> {
                            updateSearchHistory()
                        }
                        Status.FAILURE -> {

                        }
                        Status.IDLE -> {

                        }
                    }
                }
            }
        }
    }

    private fun buildSearchHistoryRecyclerView () {
        val searchHistory = viewModel.uiState.value.searchHistory
        viewBinding.rvHomepageSearchHistory.apply{
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = SearchHistoryAdapter(searchHistoryItems =  searchHistory){ productId ->
                navigator.navigateToProductDetailsPage(this@HomePage, productId)
            }
        }
    }

    private fun updateSearchHistory(){
        val searchHistory = viewModel.uiState.value.searchHistory
        val adapter = viewBinding.rvHomepageSearchHistory.adapter as SearchHistoryAdapter
        adapter.updateList(searchHistory)
    }

    private fun updateRecommendedProducts(){
        val recommendedProducts = viewModel.uiState.value.recommendedProducts
        val adapter = viewBinding.rvHomepageRecommendedProducts.adapter as RecommendedProductsAdapter
        adapter.updateList(recommendedProducts)
    }

    private fun buildRecommendedProductsRecyclerView () {
        val recommendedProducts = viewModel.uiState.value.recommendedProducts
        viewBinding.rvHomepageRecommendedProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = RecommendedProductsAdapter(recommendedProducts = recommendedProducts) { productId ->
                navigator.navigateToProductDetailsPage(this@HomePage, productId)
            }
        }
    }




  /*  private fun showProgressBar(){
        logger("trying to show progress bar")
        viewBinding.homepageMainLayout.invisible()
        viewBinding.progressbarLayout.show()
    }

    private fun hideProgressBar(){
        logger("trying to hide progress bar")
        viewBinding.homepageMainLayout.show()
        viewBinding.progressbarLayout.hide()
    }

*/
}
