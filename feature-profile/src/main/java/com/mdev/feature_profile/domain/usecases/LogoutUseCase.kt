package com.mdev.feature_profile.domain.usecases

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val profileRepository: FirebaseRepository
){
    suspend operator fun invoke(){
        profileRepository.logout()
    }
}