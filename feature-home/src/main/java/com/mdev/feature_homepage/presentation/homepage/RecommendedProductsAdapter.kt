package com.mdev.feature_homepage.presentation.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.core.utils.addImageFromUrl
import com.mdev.feature_homepage.R
import com.mdev.feature_homepage.domain.model.RecommendedProduct
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.utils.getIcon
import com.mdev.common.R as commonRes

class RecommendedProductsAdapter(
    private var recommendedProducts: List<RecommendedProduct>,
    private var callback: (String) -> Unit
    ): RecyclerView.Adapter<RecommendedProductsAdapter.RecommendedProductsViewHolder>() {

        inner class RecommendedProductsViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
            val ivRecommendedProductImage = itemView.findViewById<ImageView>(R.id.iv_recommended_product_image)
            val tvRecommendedProductName = itemView.findViewById<TextView>(R.id.tv_recommended_product_name)
            val tvRecommendedBrandName = itemView.findViewById<TextView>(R.id.tv_recommended_brand_name)
            val ivRecommendedHealthCategory = itemView.findViewById<ImageView>(R.id.iv_recommended_health_category)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_recommended_products,parent,false)
        return RecommendedProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recommendedProducts.size
    }

    override fun onBindViewHolder(holder: RecommendedProductsViewHolder, position: Int) {
        val recommendedProduct = recommendedProducts[position]
        holder.ivRecommendedProductImage.addImageFromUrl(recommendedProduct.imageUrl,commonRes.drawable.circle_moderate)
        holder.tvRecommendedProductName.text = recommendedProduct.productName
        holder.tvRecommendedBrandName.text = recommendedProduct.brandName
        holder.ivRecommendedHealthCategory.addImage(recommendedProduct.healthCategory.getIcon())
        holder.ivRecommendedProductImage.setOnClickListener {
            callback(recommendedProduct.productId)
        }
    }

    fun updateList (newList: List<RecommendedProduct>) {
        this.recommendedProducts = newList
        notifyDataSetChanged()
    }

}