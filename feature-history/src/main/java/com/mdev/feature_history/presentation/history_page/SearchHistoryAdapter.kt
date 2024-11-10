package com.mdev.feature_history.presentation.history_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdev.core.utils.TimeCalculator
import com.mdev.core.utils.logger
import com.mdev.feature_history.R
import com.mdev.feature_history.domain.model.SearchHistoryItemForView
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import java.time.LocalDateTime
import com.mdev.common.R as CommonRes

internal class SearchHistoryAdapter(
    private val searchHistoryItemForViews: List<SearchHistoryItemForView>,
    private val callback: (productId: String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {
    private var historyList = searchHistoryItemForViews
    private var filteredList = searchHistoryItemForViews

    inner class SearchHistoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivSearchHistoryImage : ImageView = itemView.findViewById(R.id.iv_search_history_image)
        val tvSearchHistoryName : TextView = itemView.findViewById(R.id.tv_search_history_name)
        val tvSearchHistoryBrand : TextView = itemView.findViewById(R.id.tv_search_history_brand)
        val tvTimeStamp : TextView = itemView.findViewById(R.id.tv_search_history_timestamp)
        val ivSearchHistoryHealthCategory : ImageView = itemView.findViewById(R.id.iv_search_history_health_category)
        val cvListItem: CardView = itemView.findViewById(R.id.cv_list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_history_list_item, parent,false)
        return  SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item = filteredList[position]
        val duration : java.time.Duration = java.time.Duration.between(item.timeStamp, LocalDateTime.now())
        val (healthCategoryIcon, _) = getHealthCategoryIcon(context = holder.ivSearchHistoryHealthCategory.context, item.healthCategory)

        holder.tvSearchHistoryName.text = item.productName
        holder.tvSearchHistoryBrand.text = item.productBrand
        holder.tvTimeStamp.text = TimeCalculator.getTime(duration)
        Glide.with(holder.ivSearchHistoryImage.context)
            .load(item.imageUrl)
            .error(CommonRes.mipmap.app_icon_small)
            .into(holder.ivSearchHistoryImage)

        Glide.with(holder.ivSearchHistoryHealthCategory.context).load(healthCategoryIcon).into(holder.ivSearchHistoryHealthCategory)
        holder.cvListItem.setOnClickListener{view ->
            view.isClickable = false
             callback(item.productId)
        }
    }
    private fun getHealthCategoryIcon(context : Context, healthCategory: HealthCategory) : Pair<Int, Int> {
        return when(healthCategory){
            HealthCategory.HEALTHY,
            HealthCategory.GOOD
            -> Pair(
                CommonRes.drawable.circle_good, ContextCompat.getColor(context,
                    CommonRes.color.product_category_good_bg
                ))
            HealthCategory.FAIR
            -> Pair(
                CommonRes.drawable.circle_moderate, ContextCompat.getColor(context,
                    CommonRes.color.product_category_moderate_bg
                ))
            HealthCategory.POOR,
            HealthCategory.BAD
            -> Pair(
                CommonRes.drawable.circle_bad, ContextCompat.getColor(context,
                    CommonRes.color.product_category_bad_bg
                ))
            HealthCategory.UNKNOWN -> Pair(
                CommonRes.drawable.circle_unknown, ContextCompat.getColor(context,
                    CommonRes.color.md_theme_background
                ))
        }
    }
    fun updateData(newList: List<SearchHistoryItemForView>) {
        this.historyList = newList
        this.filteredList = newList
        notifyDataSetChanged()
    }

    fun filter(query: String){
        logger("Filtering with query $query")
        this.filteredList = if (query.isEmpty()){
            historyList
        }else{
            this.historyList.filter {
                it.productName.contains(query, true) ||
                        it.productBrand.contains(query, true)
            }
        }
        logger("found ${this.filteredList}")
        notifyDataSetChanged()
    }

}