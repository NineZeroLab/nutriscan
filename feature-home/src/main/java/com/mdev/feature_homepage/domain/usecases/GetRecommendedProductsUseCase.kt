package com.mdev.feature_homepage.domain.usecases

import com.mdev.common.utils.Resource
import com.mdev.feature_homepage.domain.model.RecommendedProduct
import com.mdev.feature_homepage.domain.model.toRecommendedProduct
import com.mdev.feature_homepage.domain.repository.HomePageRepository
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecommendedProductsUseCase @Inject constructor(
    private val homePageRepository: HomePageRepository,
    private val getUserDetailsUseCase: GetUserDetailsUseCase
) {
    operator fun invoke(
        categories: List<String>,
        dietaryRestrictions: List<DietaryRestriction>,
        allergens: List<Allergen>
    ): Flow<Resource<List<RecommendedProduct>>> = flow {
        emit(Resource.Loading())
        try {
            val result = homePageRepository.getRecommendedProducts(
                categories ,
                dietaryRestrictions,
                allergens
            )
            val recommendedProducts = result?.map { it.toRecommendedProduct() }

            if (recommendedProducts == null){
                emit(Resource.Error("Unable to fetch Recommended Products"))
                return@flow
            }
            if (recommendedProducts.size % 2 == 1){
                emit(Resource.Success(recommendedProducts.dropLast(1)))
                return@flow
            }
            emit(Resource.Success(recommendedProducts))

        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}



