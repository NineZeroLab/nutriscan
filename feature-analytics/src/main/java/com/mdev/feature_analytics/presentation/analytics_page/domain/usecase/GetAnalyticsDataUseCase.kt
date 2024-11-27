package com.mdev.feature_analytics.presentation.analytics_page.domain.usecase

import com.mdev.client_firebase.data.remote.dto.AnalyticsData
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class GetAnalyticsDataUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke(): Flow<Resource<AnalyticsData>>  =
        firebaseRepository.getAnalyticsData()
            .map { Resource.Success(it) as Resource<AnalyticsData>}
            .flowOn(Dispatchers.IO)
            .catch { e ->
                emit(Resource.Error("Error fetching analytics data"))
            }
            .onStart { emit(Resource.Loading()) }

}
