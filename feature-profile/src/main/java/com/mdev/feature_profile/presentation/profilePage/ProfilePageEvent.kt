package com.mdev.feature_profile.presentation.profilePage

import com.mdev.client_firebase.data.remote.dto.AppUser

sealed class ProfilePageEvent {
    data class UpdateProfileDetails(val appUser: AppUser): ProfilePageEvent()
    data object GetUserProfileDetails: ProfilePageEvent()
}