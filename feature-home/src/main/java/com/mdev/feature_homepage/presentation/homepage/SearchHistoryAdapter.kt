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
import com.mdev.feature_homepage.domain.model.SearchHistoryItemForView
import com.mdev.feature_homepage.utils.getIcon
import com.mdev.common.R as commonRes


internal class SearchHistoryAdapter(
    private var searchHistoryItems: List<SearchHistoryItemForView>,
    private var callback: (String) -> Unit
): RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {
    inner class SearchHistoryViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val tvProductName: TextView = itemView.findViewById(R.id.tv_search_history_product_name)
        val tvScannedTime: TextView = itemView.findViewById(R.id.tv_seach_history_scanned_time)
        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_search_history_product_image)
        val ivHealthCategory: ImageView =
            itemView.findViewById(R.id.iv_search_history_health_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_search_history_list_item,parent,false)
        return SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchHistoryItems.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val searchHistoryItem = searchHistoryItems[position]
        holder.ivProductImage.addImageFromUrl(searchHistoryItem.imageUrl,commonRes.drawable.circle_bad)
        holder.tvProductName.text = searchHistoryItem.productName
        holder.tvScannedTime.text = searchHistoryItem.scannedTime
        holder.ivHealthCategory.addImage(searchHistoryItem.healthCategory.getIcon())
        holder.ivProductImage.setOnClickListener {
            callback(searchHistoryItem.productId)
        }
    }

    fun updateList(newList: List<SearchHistoryItemForView>) {
        this.searchHistoryItems = newList
        notifyDataSetChanged()
    }
}