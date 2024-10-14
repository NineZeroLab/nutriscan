package com.mdev.feature_history.domain.usecases

import com.mdev.client_firebase.data.remote.dto.ProductDetailsDto
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<ProductDetailsDto>>> = flow{
        emit(Resource.Loading())
        try {
            firebaseRepository.getSearchHistory().collect{ searchHistory ->
                emit(Resource.Success(searchHistory))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}