package com.mdev.feature_product_details.domain.usecases

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.domain.model.RecommendedProduct
import com.mdev.feature_product_details.domain.model.toRecommendedProducts
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetRecommendedProductsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<RecommendedProduct>>>  = flow{
        emit(Resource.Loading())
        val appUser = firebaseRepository.getCurrentUserDetails()
        if (appUser == null){
            emit(Resource.Error("Unable to fetch current user details"))
        }else{
            try {
                val recommendedProducts = productDetailsRepository.getRecommendedProducts(appUser.dietaryRestrictions,appUser.allergens)
                recommendedProducts?.let {
                    logger("recommended product fetch success")
                    logger(recommendedProducts.map { it.productName }.toString())
                    emit(Resource.Success(recommendedProducts.toRecommendedProducts()))
                }
            }catch (e: Exception){
                logger("recommended product fetch failure")
                logger(e.message.toString())
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}