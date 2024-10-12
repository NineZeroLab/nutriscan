package com.mdev.feature_homepage.domain.usecases

import com.mdev.feature_homepage.domain.repository.HomePageRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository
){
    suspend operator fun invoke(){
        homePageRepository.logOut()
    }
}