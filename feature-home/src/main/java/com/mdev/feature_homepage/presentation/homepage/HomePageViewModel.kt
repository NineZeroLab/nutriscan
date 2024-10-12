package com.mdev.feature_homepage.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.domain.model.Status
import com.mdev.feature_homepage.domain.model.SearchHistoryListItem
import com.mdev.feature_homepage.domain.usecases.FetchSearchHistoryUseCase
import com.mdev.feature_homepage.domain.usecases.FetchUserDetailsUseCase
import com.mdev.feature_homepage.domain.usecases.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
     * Think of efficiently collecting values and stop collecting when the view is not started
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
        searchHistoryCollectionJob.start()
        updateUserDetails()
    }
    fun onEvent(event: HomePageEvent){
        when(event){
            HomePageEvent.LogOut -> logOut()
        }
    }
    private fun updateUserDetails(){
        viewModelScope.launch {
            val appUser = fetchUserDetailsUseCase()
            _uiState.update {
                it.copy(appUser = appUser)
            }
        }
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