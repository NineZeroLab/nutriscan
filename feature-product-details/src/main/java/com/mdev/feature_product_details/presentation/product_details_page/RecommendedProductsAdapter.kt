package com.mdev.feature_product_details.presentation.product_details_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImageFromUrl
import com.mdev.core.utils.logger
import com.mdev.feature_product_details.R
import com.mdev.feature_product_details.domain.model.RecommendedProduct
import com.mdev.common.R as CommonRes
import com.mdev.openfoodfacts_client.data.remote.dto.RecommendedProductDto

internal class RecommendedProductsAdapter(private val recommendedProducts: List<RecommendedProduct>):
    RecyclerView.Adapter<RecommendedProductsAdapter.RecommendedProductViewHolder>() {

    inner class RecommendedProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_rp_image)
        val tvProductName: TextView = itemView.findViewById(R.id.tv_rp_name)
        val tvProductBrand: TextView = itemView.findViewById(R.id.tv_rp_brand)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_recommended_product_list_item, parent, false)
        return RecommendedProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recommendedProducts.size
    }

    override fun onBindViewHolder(holder: RecommendedProductViewHolder, position: Int) {
        val product = recommendedProducts[position]
        holder.ivProductImage.addImageFromUrl(product.imageUrl.toString(), errorImage = CommonRes.mipmap.app_icon_small)
        holder.tvProductName.text = product.name
        holder.tvProductBrand.text = product.nutriScoreGrade
    }

}