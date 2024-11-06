package com.mdev.feature_homepage.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_homepage.domain.model.RecommendedProduct
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.domain.usecases.GetRecommendedProductsUseCase
import com.mdev.feature_homepage.domain.usecases.GetSearchHistoryUseCase
import com.mdev.feature_homepage.domain.usecases.GetUserDetailsUseCase
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HomePageState(
    val appUser: AppUser? = null,
    val searchHistory: List<SearchHistoryItem> = emptyList(),
    val recommendedProducts: List<RecommendedProduct> = emptyList(),
    val appUserDataFetchState: Status = Status.IDLE,
    val searchHistoryFetchState: Status = Status.IDLE,
    val recommendedProductFetchState: Status = Status.IDLE,
    val errorMessage: String? = null,
)

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val getUserDetailsUseCase: GetUserDetailsUseCase,
    private val getRecommendedProductsUseCase: GetRecommendedProductsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomePageState())
    val uiState = _uiState.asStateFlow()
    init{
        getSearchHistory()
        getUserDetails()
    }
    private fun getUserDetails() {
        getUserDetailsUseCase().onEach { resource ->
            when(resource){
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            appUserDataFetchState = Status.FAILURE,
                            errorMessage = resource.message
                        )
                    }
                    _uiState.update {
                        it.copy(
                            appUserDataFetchState = Status.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            appUserDataFetchState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            appUserDataFetchState = Status.SUCCESS,
                            appUser = resource.data
                        )
                    }
                    _uiState.update {
                        it.copy(
                            appUserDataFetchState = Status.IDLE
                        )
                    }
                    val dietaryRestrictions = resource.data?.dietaryRestrictions ?: emptyList()
                    val allergens = resource.data?.allergens ?: emptyList()
                    getRecommendedProducts(
                        categories = listOf("en:snacks"),
                        dietaryRestrictions = dietaryRestrictions,
                        allergens = allergens
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSearchHistory() {
        logger("fetching search history from viewmodel")
        getSearchHistoryUseCase().onEach { resource ->
            when(resource){
                is Resource.Error -> {
                    logger("Error fetching search history in viewmodel")
                    _uiState.update {
                        it.copy(
                            errorMessage = resource.message,
                            searchHistoryFetchState = Status.FAILURE
                        )
                    }
                    _uiState.update {
                        it.copy(
                            searchHistoryFetchState = Status.IDLE
                        )
                    }

                }
                is Resource.Loading -> {
                    logger("Loading search history in viewmodel")
                    _uiState.update {
                        it.copy(
                            searchHistoryFetchState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    logger("success fetching search history in viewmodel")
                    resource.data?.let { searchHistory ->
                        _uiState.update {
                            it.copy(
                                searchHistoryFetchState = Status.SUCCESS,
                                searchHistory = searchHistory
                            )
                        }
                        _uiState.update {
                            it.copy(
                                searchHistoryFetchState = Status.IDLE
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecommendedProducts(
        categories: List<String>,
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ){
        getRecommendedProductsUseCase(
            categories, dietaryRestrictions, allergens
        ).onEach { resource ->
            when(resource){
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            recommendedProductFetchState = Status.FAILURE,
                            errorMessage = resource.message
                        )
                    }
                    _uiState.update {
                        it.copy(
                            recommendedProductFetchState = Status.IDLE
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            recommendedProductFetchState = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            recommendedProductFetchState = Status.SUCCESS,
                            recommendedProducts = resource.data ?: emptyList()
                        )
                    }
                    _uiState.update {
                        it.copy(
                            recommendedProductFetchState = Status.IDLE
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}