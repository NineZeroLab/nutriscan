package com.mdev.feature_product_details.domain.usecases

import com.mdev.feature_product_details.domain.model.Considerations
import com.mdev.feature_product_details.domain.model.getUserConsideration
import com.mdev.feature_product_details.domain.repository.ProductDetailsRepository
import javax.inject.Inject

internal class GetUserConsiderationsUseCase @Inject constructor(
    private val productDetailsRepository: ProductDetailsRepository
) {
    internal suspend operator fun invoke(): Considerations?{
        return productDetailsRepository.getUserPreference()?.getUserConsideration()
    }
}