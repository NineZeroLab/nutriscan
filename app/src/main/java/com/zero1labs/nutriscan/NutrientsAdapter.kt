package com.zero1labs.nutriscan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.zero1labs.nutriscan.data.models.HealthCategory
import com.zero1labs.nutriscan.data.models.NutrientType
import com.zero1labs.nutriscan.data.models.ProductDetailsListItems

class NutrientsAdapter(private val productDetailsListItems: List<ProductDetailsListItems>) : Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val VIEW_TYPE_MAIN_HEADER = 1
        private const val VIEW_TYPE_NUTRIENTS = 2
        private const val VIEW_TYPE_NUTRIENTS_HEADER = 3
    }

    class NutrientsHeaderViewHolder(itemView: View) : ViewHolder(itemView){
        val tvNutrientsHeader : TextView = itemView.findViewById(R.id.tv_nutrients_header)
    }

    class ProductHeaderViewHolder(itemView: View) : ViewHolder(itemView){
        val tvProductName : TextView = itemView.findViewById(R.id.tv_product_name)
        val tvProductBrand : TextView = itemView.findViewById(R.id.tv_product_brand)
        val ivProductImage : ImageView = itemView.findViewById(R.id.iv_product_image)
        val tvProductGrade : TextView = itemView.findViewById(R.id.tv_product_health_grade)
        val ivProductHealthIcon : ImageView = itemView.findViewById(R.id.iv_product_health_icon)
    }

    class NutrientsViewHolder(itemView: View) : ViewHolder(itemView) {
        val cvNutrientCard : CardView = itemView.findViewById(R.id.cardView)
        val tvNutrientName : TextView = itemView.findViewById(R.id.tv_nutrient_name)
        val tvNutrientDescription : TextView = itemView.findViewById(R.id.tv_nutrient_description)
        val tvNutrientPerHundredGram : TextView = itemView.findViewById(R.id.tv_per_hundred_gram)
        val ivNutrientIcon : ImageView = itemView.findViewById(R.id.nutrient_icon)
        val ivNutrientCategoryIcon : ImageView = itemView.findViewById(R.id.nutrient_category_icon)

    }

    override fun getItemViewType(position: Int): Int {
        return when(productDetailsListItems[position]){
            is ProductDetailsListItems.NegativeNutrientsForView -> VIEW_TYPE_NUTRIENTS
            is ProductDetailsListItems.PositiveNutrientsForView -> VIEW_TYPE_NUTRIENTS
            is ProductDetailsListItems.ProductHeader -> VIEW_TYPE_MAIN_HEADER
            is ProductDetailsListItems.NutrientsHeaderForView -> VIEW_TYPE_NUTRIENTS_HEADER
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            VIEW_TYPE_NUTRIENTS -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_ingredient_item,parent,false)
                return NutrientsViewHolder(view)
            }
            VIEW_TYPE_MAIN_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.product_details_main_header,parent, false)
                return ProductHeaderViewHolder(view)
        }
            VIEW_TYPE_NUTRIENTS_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.nutrients_header, parent, false)
                return NutrientsHeaderViewHolder(view)
            }

            else -> { throw IllegalArgumentException("Invalid View Type")}
        }
    }

    override fun getItemCount(): Int {
        return productDetailsListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = productDetailsListItems[position]){
            is ProductDetailsListItems.ProductHeader ->{
                (holder as ProductHeaderViewHolder).let { productHeaderViewHolder ->
                    productHeaderViewHolder.tvProductName.text = item.mainDetailsForView.productName
                    productHeaderViewHolder.tvProductBrand.text = item.mainDetailsForView.productBrand
                    productHeaderViewHolder.tvProductGrade.text = item.mainDetailsForView.healthCategory.description
                    Glide.with(productHeaderViewHolder.ivProductImage.context).load(item.mainDetailsForView.imageUrl).into(productHeaderViewHolder.ivProductImage)
                    Glide.with(productHeaderViewHolder.ivProductHealthIcon.context).load(getHealthCategoryIcon(item.mainDetailsForView.healthCategory)).into(productHeaderViewHolder.ivProductHealthIcon)
                }
            }

            is ProductDetailsListItems.NegativeNutrientsForView -> {
                (holder as NutrientsViewHolder).apply {
                    tvNutrientName.text = item.nutrient.nutrientType.heading
                    tvNutrientDescription.text = item.nutrient.description
                    tvNutrientPerHundredGram.text = item.nutrient.let {nutrient ->
                        "${nutrient.contentPerHundredGrams} ${nutrient.servingUnit}"
                    }
                    Glide.with(ivNutrientIcon.context).load(getNutrientIcon(item.nutrient.nutrientType))
                        .into(ivNutrientIcon)

                    Glide.with(ivNutrientCategoryIcon.context).load(getHealthCategoryIcon(item.nutrient.healthCategory))
                        .into(ivNutrientCategoryIcon)

                }
            }
            is ProductDetailsListItems.PositiveNutrientsForView -> {
                (holder as NutrientsViewHolder).apply {
                    tvNutrientName.text = item.nutrient.nutrientType.heading
                    tvNutrientDescription.text = item.nutrient.description
                    tvNutrientPerHundredGram.text = item.nutrient.let {nutrient ->
                        "${nutrient.contentPerHundredGrams} ${nutrient.servingUnit}"
                    }
                    Glide.with(ivNutrientIcon.context).load(getNutrientIcon(item.nutrient.nutrientType)).into(ivNutrientIcon)
                    Glide.with(ivNutrientCategoryIcon.context).load(getHealthCategoryIcon(item.nutrient.healthCategory)).into(ivNutrientCategoryIcon)

                    cvNutrientCard.setOnClickListener {
                        it.rootView.findViewById<TextView>(R.id.tv_product_brand).text = NutrientType.values().random().name
                    }
                }
            }
            is ProductDetailsListItems.NutrientsHeaderForView -> {
                (holder as NutrientsHeaderViewHolder).apply {
                    tvNutrientsHeader.text = item.nutrientCategory.header
                }
            }
        }

    }
    private fun getHealthCategoryIcon(healthCategory: HealthCategory) : Int {
        return when(healthCategory){
            HealthCategory.HEALTHY -> R.drawable.circle_dark_green
            HealthCategory.GOOD -> R.drawable.circle_light_green
            HealthCategory.FAIR -> R.drawable.circle_yellow
            HealthCategory.POOR -> R.drawable.circle_orange
            HealthCategory.BAD -> R.drawable.circle_red
            HealthCategory.UNKNOWN -> R.drawable.circle_gray
        }
    }

    private fun getNutrientIcon(nutrientType: NutrientType) : Int {
        return  when(nutrientType){
            NutrientType.ENERGY ->  R.mipmap.person_outline_24px
            NutrientType.PROTEIN -> R.mipmap.person_outline_24px
            NutrientType.SATURATES -> R.mipmap.person_outline_24px
            NutrientType.SUGAR -> R.mipmap.person_outline_24px
            NutrientType.FIBRE -> R.mipmap.person_outline_24px
            NutrientType.SODIUM -> R.mipmap.person_outline_24px
            NutrientType.FRUITS_VEGETABLES_AND_NUTS -> R.mipmap.person_outline_24px
        }
    }
}