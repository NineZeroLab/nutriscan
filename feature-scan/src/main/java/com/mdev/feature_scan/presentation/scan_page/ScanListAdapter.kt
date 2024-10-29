package com.mdev.feature_scan.presentation.scan_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.core.utils.addImageFromUrl
import com.mdev.feature_scan.R
import com.mdev.common.R as CommonRes
import com.mdev.feature_scan.domain.model.ProductDetailsForView

internal class ScanListAdapter(private var scanList: List<ProductDetailsForView>): RecyclerView.Adapter<ScanListAdapter.ScanListViewHolder>() {

    inner class ScanListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivScanItemImage: ImageView = itemView.findViewById(R.id.iv_scan_item_image)
        val tvScanItemName: TextView = itemView.findViewById(R.id.tv_scan_item_name)
        val tvScanItemBrand: TextView = itemView.findViewById(R.id.tv_scan_item_brand)
        val ivScanItemHealthCategory: ImageView = itemView.findViewById(R.id.iv_scan_item_health_category)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_scan_list_item, parent, false)
        return ScanListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scanList.size
    }

    override fun onBindViewHolder(holder: ScanListViewHolder, position: Int) {
        val item = scanList[position]
        holder.apply {
            tvScanItemName.text = item.name
            ivScanItemImage.addImageFromUrl(item.imageUrl, CommonRes.mipmap.app_icon_small)
            ivScanItemHealthCategory.addImage(CommonRes.drawable.circle_bad)
            tvScanItemBrand.text = item.brand
        }
    }
    fun addItemToList(item: ProductDetailsForView){
        val newList = scanList.toMutableList()
        newList.add(item)
        this.scanList = newList
        notifyItemInserted(this.scanList.size)
    }
}