package com.mdev.feature_homepage.domain.usecases

import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import javax.inject.Inject

internal class FetchUserDetailsUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
) {
    internal suspend operator fun invoke(): AppUser? {
        return homePageRepository.getUserDetails()
    }
}