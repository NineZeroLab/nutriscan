package com.mdev.feature_homepage.presentation.homepage

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.core.utils.addImageFromUrl
import com.mdev.feature_homepage.R
import com.mdev.feature_homepage.domain.model.SearchHistoryItem
import com.mdev.feature_homepage.utils.getIcon
import com.mdev.common.R as commonRes


class SearchHistoryAdapter(
    private var searchHistoryItems: List<SearchHistoryItem>,
    private var callback: (String) -> Unit
): RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {
    inner class SearchHistoryViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val tvProductName = itemView.findViewById<TextView>(R.id.tv_search_history_product_name)
        val tvScannedTime = itemView.findViewById<TextView>(R.id.tv_search_history_scanned_time)
        val ivProductImage = itemView.findViewById<ImageView>(R.id.iv_search_history_product_image)
        val ivHealthCategory = itemView.findViewById<ImageView>(R.id.iv_search_history_health_category)
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
        holder.tvScannedTime.text = searchHistoryItem.scannedTime.toString()
        holder.ivHealthCategory.addImage(searchHistoryItem.healthCategory.getIcon())
        holder.ivProductImage.setOnClickListener {
            callback(searchHistoryItem.productId)
        }
    }

    fun updateList (newList: List<SearchHistoryItem>) {
        this.searchHistoryItems = newList
        notifyDataSetChanged()
    }
}