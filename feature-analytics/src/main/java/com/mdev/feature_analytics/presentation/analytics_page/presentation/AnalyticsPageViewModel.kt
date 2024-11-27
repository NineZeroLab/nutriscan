package com.mdev.feature_analytics.presentation.analytics_page.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.client_firebase.data.remote.dto.AnalyticsData
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.logger
import com.mdev.feature_analytics.presentation.analytics_page.domain.usecase.GetAnalyticsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsPageState(
    val analyticsData: AnalyticsData? = null,
    val analyticsDataFetchState: Status = Status.IDLE,
    val errorMessage: String? = null
)

@HiltViewModel
internal class AnalyticsPageViewModel @Inject constructor(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnalyticsPageState())
    val uiState = _uiState.asStateFlow()

    init {
        getAnalyticsData()
    }

    fun onEvent(event: AnalyticsPageEvent) {
        when (event) {
            AnalyticsPageEvent.GetAnalyticsData -> getAnalyticsData()
        }
    }

    private fun getAnalyticsData() {
        logger("AnalyticsViewModel: Fetching analytics data...")
        viewModelScope.launch {
            getAnalyticsDataUseCase().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                analyticsDataFetchState = Status.FAILURE,
                                errorMessage = result.message
                            )
                        }
                        _uiState.update {
                            it.copy(
                                analyticsDataFetchState = Status.IDLE
                            )
                        }

                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                analyticsDataFetchState = Status.LOADING
                            )
                        }

                    }

                    is Resource.Success -> {
                        result.data?.let { analyticsData ->
                            _uiState.update {
                                it.copy(
                                    analyticsDataFetchState = Status.SUCCESS,
                                    analyticsData = analyticsData
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    analyticsDataFetchState = Status.IDLE
                                )
                            }
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }
}