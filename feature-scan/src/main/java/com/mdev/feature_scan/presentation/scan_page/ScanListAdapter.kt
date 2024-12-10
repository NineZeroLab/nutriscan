package com.mdev.feature_scan.presentation.scan_page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mdev.core.utils.addImage
import com.mdev.core.utils.addImageFromUrl
import com.mdev.feature_scan.R
import com.mdev.common.R as CommonRes
import com.mdev.feature_scan.domain.model.ScanItemForView
import com.mdev.openfoodfacts_client.domain.model.getIcon

internal class ScanListAdapter(private var scanList: List<ScanItemForView>, private val callback: (productId: String) -> Unit): RecyclerView.Adapter<ScanListAdapter.ScanListViewHolder>() {

    inner class ScanListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivScanItemImage: ImageView = itemView.findViewById(R.id.iv_scan_item_image)
        val tvScanItemName: TextView = itemView.findViewById(R.id.tv_scan_item_name)
        val tvScanItemBrand: TextView = itemView.findViewById(R.id.tv_scan_item_brand)
        val ivScanItemHealthCategory: ImageView = itemView.findViewById(R.id.iv_scan_item_health_category)
        val cvScanItem: CardView = itemView.findViewById(R.id.cv_scan_item)
        val ivPalOilStatus: ImageView = itemView.findViewById(R.id.iv_scan_item_palm_oil_status)
        val ivVeganStatus: ImageView = itemView.findViewById(R.id.iv_scan_item_vegan_status)
        val ivVegetarianStatus: ImageView = itemView.findViewById(R.id.iv_scan_item_vegetarian_status)
        val tvAllergenCount: TextView = itemView.findViewById(R.id.tv_scan_item_allergen_count)
        val tvAdditiveCount: TextView = itemView.findViewById(R.id.tv_scan_item_additives_count)
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

            ivPalOilStatus.setImageDrawable(item.palmOilStatus.getIcon(ivPalOilStatus.context,false))
            ivVeganStatus.setImageDrawable(item.veganStatus.getIcon(ivVeganStatus.context,false))
            ivVegetarianStatus.setImageDrawable(item.vegetarianStatus.getIcon(ivVegetarianStatus.context,false))

            val additivesCount = "Additives: ${item.additiveCount}"
            val allergenCount = "Allergens: ${item.allergenCount}"

            tvAdditiveCount.text = additivesCount
            tvAllergenCount.text = allergenCount

            cvScanItem.setOnClickListener {
                callback(item.id)
            }
        }
    }
    fun addItemToList(item: ScanItemForView){
        val newList = scanList.toMutableList()
        newList.add(item)
        this.scanList = newList
        notifyItemInserted(this.scanList.size)
    }
}