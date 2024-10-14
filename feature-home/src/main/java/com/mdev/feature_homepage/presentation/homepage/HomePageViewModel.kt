package com.mdev.feature_homepage.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.Resource
import com.mdev.common.utils.domain.model.Status
import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import com.mdev.feature_homepage.domain.usecases.FetchSearchHistoryUseCase
import com.mdev.feature_homepage.domain.usecases.FetchUserDetailsUseCase
import com.mdev.feature_homepage.domain.usecases.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomePageState(
    val errorMessage: String? = null,
    val searchHistory: List<SearchHistoryListItem> = mutableListOf(),
    val searchHistoryFetchStatus: Status = Status.LOADING,
    val appUser: AppUser? = null,
)

@HiltViewModel
internal class HomePageViewModel @Inject constructor(
    private val fetchSearchHistoryUseCase: FetchSearchHistoryUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val fetchUserDetailsUseCase: FetchUserDetailsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(HomePageState())
    val uiState = _uiState.asStateFlow()
    /**
     * Think of ways to efficiently collect values
     */
    private val searchHistoryCollectionJob = viewModelScope.launch {
        fetchSearchHistoryUseCase().collect{ searchHistoryList ->
            _uiState.update { uiState ->
                uiState.copy(
                    searchHistory = searchHistoryList
                )
            }
        }
    }

    init {
        updateUserDetails()
        searchHistoryCollectionJob.start()
    }
    fun onEvent(event: HomePageEvent){
        when(event){
            HomePageEvent.LogOut -> logOut()
        }
    }
    private fun updateUserDetails(){
        fetchUserDetailsUseCase().onEach { result ->
            when(result){
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            searchHistoryFetchStatus = Status.LOADING
                        )
                    }
                }
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            appUser = result.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun logOut() {
        viewModelScope.launch {
            logOutUseCase()
            _uiState.update {
                HomePageState()
            }
        }
    }


    fun startCollectingHistory(){
        searchHistoryCollectionJob.start()
    }

    fun stopCollectingHistory(){
        searchHistoryCollectionJob.cancel()
    }
}