package com.zero1labs.nutriscan.pages.homepage

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
import com.bumptech.glide.request.RequestOptions
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.utils.HealthCategory
import com.zero1labs.nutriscan.data.models.SearchHistoryListItem
import com.zero1labs.nutriscan.utils.TimeCalculator
import java.time.LocalDateTime
import java.time.ZoneId

class SearchHistoryAdapter(private val viewModel: HomePageViewModel ,private var searchHistoryItems : List<SearchHistoryListItem>) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {
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
        return searchHistoryItems.size
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item = searchHistoryItems[position]
        val duration : java.time.Duration = java.time.Duration.between(LocalDateTime.ofInstant(item.timeStamp.toInstant(),
            ZoneId.systemDefault()), LocalDateTime.now())
        val (healthCategoryIcon, _) = getHealthCategoryIcon(context = holder.ivSearchHistoryHealthCategory.context, item.mainDetailsForView.healthCategory)

        holder.tvSearchHistoryName.text = item.mainDetailsForView.productName
        holder.tvSearchHistoryBrand.text = item.mainDetailsForView.productBrand
        holder.tvTimeStamp.text = TimeCalculator.getTime(duration)
        Glide.with(holder.ivSearchHistoryImage.context)
            .load(item.mainDetailsForView.imageUrl)
            .error(R.mipmap.app_icon_small)
            .into(holder.ivSearchHistoryImage)

        Glide.with(holder.ivSearchHistoryHealthCategory.context).load(healthCategoryIcon).into(holder.ivSearchHistoryHealthCategory)
        holder.cvListItem.setOnClickListener{view ->
            view.isClickable = false
            viewModel.onEvent(HomePageEvent.FetchProductDetails(item.mainDetailsForView.productId ?: ""))
        }
    }
    private fun getHealthCategoryIcon(context : Context, healthCategory: HealthCategory) : Pair<Int, Int> {
        return when(healthCategory){
            HealthCategory.HEALTHY,
            HealthCategory.GOOD
            -> Pair(
                R.drawable.circle_good, ContextCompat.getColor(context,
                    R.color.product_category_good_bg
                ))
            HealthCategory.FAIR
            -> Pair(
                R.drawable.circle_moderate, ContextCompat.getColor(context,
                    R.color.product_category_moderate_bg
                ))
            HealthCategory.POOR,
            HealthCategory.BAD
            -> Pair(
                R.drawable.circle_bad, ContextCompat.getColor(context,
                    R.color.product_category_bad_bg
                ))
            HealthCategory.UNKNOWN -> Pair(
                R.drawable.circle_unknown, ContextCompat.getColor(context,
                    R.color.md_theme_background
                ))
        }
    }
    fun updateData(newList: List<SearchHistoryListItem>){
        this.searchHistoryItems = newList
        notifyDataSetChanged()
    }

}