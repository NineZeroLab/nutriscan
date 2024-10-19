package com.mdev.feature_product_details.presentation.product_details_page

import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.common.utils.Resource
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetRecommendedProductsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository,
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): Flow<Resource<List<RecommendedProductDto>>>  = flow{
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
                    emit(Resource.Success(recommendedProducts))
                }
            }catch (e: Exception){
                logger("recommended product fetch failure")
                logger(e.message.toString())
                emit(Resource.Error(e.message.toString()))
            }
        }
    }
}