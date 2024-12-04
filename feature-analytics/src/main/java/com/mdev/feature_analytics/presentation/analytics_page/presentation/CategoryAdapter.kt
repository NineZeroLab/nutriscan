package com.mdev.feature_analytics.presentation.analytics_page.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.feature_analytics.R
import com.mdev.common.R as CommonRes

class CategoryAdapter(private val categories: List<Pair<String, Int>>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivCategoryImage: ImageView = itemView.findViewById(R.id.iv_analytics_category_image)
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_analytics_category_name)
        val tvCategoryCount: TextView = itemView.findViewById(R.id.tv_analytics_category_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_analytics_category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categories[position]

        holder.apply {
            ivCategoryImage.addImage(item.first.getCategory())
            tvCategoryName.text = item.first
            tvCategoryCount.text = item.second.toString()
        }
    }
}


private fun String.getCategory(): Int {
    return when {
        contains("plant") -> CommonRes.mipmap.plant_based_food
        contains("nuts") -> CommonRes.mipmap.nuts
        contains("cereal") -> CommonRes.mipmap.cereal
        contains("confectionary") -> CommonRes.mipmap.confectionary
        contains("beverages") -> CommonRes.mipmap.beverages
        else -> CommonRes.mipmap.snacks
    }

}