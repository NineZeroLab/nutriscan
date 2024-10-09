package com.mdev.feature_profile.presentation.profilePage

import androidx.lifecycle.ViewModel
import com.mdev.client_firebase.data.remote.dto.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfilePageState(
    val profileUpdateStatus: ProfileUpdateStatus = ProfileUpdateStatus.IDLE,
    val appUser: AppUser? = null,
)
enum class ProfileUpdateStatus{
    LOADING,
    SUCCESS,
    FAILURE,
    IDLE
}

@HiltViewModel
class ProfilePageViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(ProfilePageState())
    val uiState = _uiState.asStateFlow()


}