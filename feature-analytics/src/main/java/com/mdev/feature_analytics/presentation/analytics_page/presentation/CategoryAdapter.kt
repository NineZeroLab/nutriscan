package com.mdev.feature_analytics.presentation.analytics_page.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.feature_analytics.R

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
            ivCategoryImage.addImage(com.mdev.common.R.mipmap.app_icon)
            tvCategoryName.text = item.first
            tvCategoryCount.text = item.second.toString()
        }
    }
}