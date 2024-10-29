package com.mdev.feature_product_details.domain.usecases

import com.mdev.feature_product_details.domain.model.Considerations
import com.mdev.feature_product_details.domain.model.UserConclusion
import com.mdev.feature_product_details.domain.model.getConclusion
import javax.inject.Inject

internal class CalculateProductConclusionUseCase @Inject constructor(
    private val getUserConsiderationsUseCase: GetUserConsiderationsUseCase
) {
    internal suspend operator fun invoke(productConsiderations: Considerations): UserConclusion?{
        val userConsideration = getUserConsiderationsUseCase()
        userConsideration?.let {
            return it.getConclusion(productConsiderations = productConsiderations)
        }
        return null
    }
}