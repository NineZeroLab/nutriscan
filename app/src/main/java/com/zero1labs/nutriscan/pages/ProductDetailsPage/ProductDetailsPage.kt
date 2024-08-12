package com.zero1labs.nutriscan.pages.ProductDetailsPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.Nutrient
import com.zero1labs.nutriscan.utils.NutrientCategory
import com.zero1labs.nutriscan.data.models.NutrientGenerator
import com.zero1labs.nutriscan.databinding.FragmentHomePageBinding
import com.zero1labs.nutriscan.databinding.FragmentProductDetailsPageBinding
import com.zero1labs.nutriscan.pages.homepage.HomePageViewModel
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.pages.homepage.ProductScanState
import com.zero1labs.nutriscan.utils.Allergen
import com.zero1labs.nutriscan.utils.HealthCategory
import com.zero1labs.nutriscan.utils.NutrientType
import com.zero1labs.nutriscan.utils.ProductType
import kotlinx.coroutines.launch

class ProductDetailsPage : Fragment(R.layout.fragment_product_details_page) {

    private lateinit var llProductDetailsLayout: LinearLayout
    private lateinit var viewBinding: FragmentProductDetailsPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        viewBinding = FragmentProductDetailsPageBinding.inflate(inflater, container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        val appCompatActivity: AppCompatActivity = activity as AppCompatActivity
        val materialToolbar: MaterialToolbar = appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)
        val navController = findNavController()
        materialToolbar.setupWithNavController(navController)
        materialToolbar.title = "Product Details"



//        viewBinding.llProductDetailsLayout.
        llProductDetailsLayout = view.findViewById(R.id.ll_product_details_layout)

        viewLifecycleOwner.lifecycleScope.launch {
        viewModel.uiState.collect{state ->
            when(state.productScanState){
                ProductScanState.Success -> {
                    Log.d("logger" , "scan status updated in viewModel")
                    }
                ProductScanState.Failure -> {
                    Log.d("logger" , "product data fetching error in viewModel")
                    findNavController().navigate(R.id.action_productDetailsPageLayout_to_ProductFetchErrorPage)

                    }
                ProductScanState.Loading -> {
                    Log.d("logger" , "loading product details in viewModel")
                    }
                ProductScanState.NotStarted -> Log.d("logger", "product scan not started")
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                state.product?.let {product ->
                    val userDietaryPreference = state.appUser?.dietaryPreferences
                    val userDietaryRestrictions = state.appUser?.dietaryRestrictions
                    val userAllergens = state.appUser?.allergens
                    val nutrientGenerator = NutrientGenerator(product)
                    val negativeNutrientsForView = nutrientGenerator.generateNutrientsForView(NutrientCategory.NEGATIVE)
                    val positiveNutrientsForView = nutrientGenerator.generateNutrientsForView(NutrientCategory.POSITIVE)
                    val allergensForView = AppResources.getAllergens(product.allergensHierarchy)


                    val productDietaryPreferences =  nutrientGenerator.getNutrientPreference()
                    val productDietaryRestrictions = AppResources.getDietaryRestrictions(product.ingredientsAnalysisTags)
                    val dietaryPreferenceConclusion = AppResources.getDietaryPreferenceConclusion(productDietaryPreferences,userDietaryPreference)
                    val dietaryRestrictionConclusion = AppResources.getDietaryRestrictionConclusion(productDietaryRestrictions,userDietaryRestrictions)
                    val allergenConclusion = AppResources.getAllergenConclusion(allergensForView,userAllergens)

                    buildMainHeader(
                        mainDetailsForView = MainDetailsForView.getMainDetailsForView(product),
                        dietaryPreferenceConclusion = dietaryPreferenceConclusion,
                        dietaryRestrictionConclusion = dietaryRestrictionConclusion,
                        allergenConclusion = allergenConclusion
                    )
                    if (negativeNutrientsForView.isNotEmpty()){
                        buildNutrientsView(
                            nutrientCategory = NutrientCategory.NEGATIVE,
                            productType = AppResources.getProductType(product.categoriesHierarchy),
                            nutrientsForView = negativeNutrientsForView
                        )
                    }
                    if (positiveNutrientsForView.isNotEmpty()){
                        buildNutrientsView(
                            nutrientCategory = NutrientCategory.POSITIVE,
                            productType = AppResources.getProductType(product.categoriesHierarchy),
                            nutrientsForView = positiveNutrientsForView
                        )
                    }
                    if (allergensForView.isNotEmpty()){
//                        buildAllergensView(AppResources.getAllergens(product.allergensHierarchy))
                    }
                }
            }
        }
    }

    private fun buildAllergensView(allergens: List<Allergen>) {
        val headerView = LayoutInflater.from(requireContext()).inflate(R.layout.nutrients_header, llProductDetailsLayout,false)
        val tvAllergenHeader: TextView = headerView.findViewById(R.id.tv_nutrients_header)
        headerView.findViewById<TextView>(R.id.serving_quantity).text = allergens.size.toString()
        tvAllergenHeader.text = "Allergens"
        llProductDetailsLayout.addView(headerView)

        allergens.forEach { allergen ->
            val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_ingredient_item, llProductDetailsLayout, false)
            val tvAllergenName: TextView = itemView.findViewById(R.id.tv_nutrient_name)
            itemView.findViewById<TextView>(R.id.tv_nutrient_description).text = ""
            itemView.findViewById<TextView>(R.id.tv_per_hundred_gram).text = ""
            itemView.findViewById<ImageView>(R.id.nutrient_category_icon).setImageIcon(null)
            itemView.findViewById<TextView>(R.id.tv_nutrient_description).text = ""
            tvAllergenName.text = allergen.heading

            llProductDetailsLayout.addView(itemView)
        }
    }

    private fun buildMainHeader(
        mainDetailsForView: MainDetailsForView,
        dietaryPreferenceConclusion: String,
        dietaryRestrictionConclusion: String,
        allergenConclusion: String
    ){
        val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.product_details_main_header, llProductDetailsLayout,false)
        val cvProductHealthCategory: CardView = itemView.findViewById(R.id.cv_product_health)
        val ivProductImageView: ImageView = itemView.findViewById(R.id.iv_product_image)
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvProductBrand: TextView = itemView.findViewById(R.id.tv_product_brand)
        val ivProductHealthIcon: ImageView = itemView.findViewById(R.id.iv_product_health_icon)
        val tvProductHealthGrade: TextView = itemView.findViewById(R.id.tv_product_health_grade)
        val tvDietaryPreferenceConclusion: TextView = itemView.findViewById(R.id.tv_dietary_preference_conclusion)
        val tvDietaryRestrictionConclusion: TextView = itemView.findViewById(R.id.tv_dietary_restriction_conclusion)
        val tvAllergenConclusion: TextView = itemView.findViewById(R.id.tv_allergen_conclusion)

        val (healthCategoryIcon, healthCategoryBg) = getHealthCategoryIcon(
            requireContext(),mainDetailsForView.healthCategory
        )
        tvProductName.text = mainDetailsForView.productName
        tvProductBrand.text = mainDetailsForView.productBrand
        tvProductHealthGrade.text = mainDetailsForView.healthCategory.description
        tvDietaryPreferenceConclusion.text = dietaryPreferenceConclusion
        tvDietaryRestrictionConclusion.text = dietaryRestrictionConclusion
        tvAllergenConclusion.text = allergenConclusion
        Glide.with(ivProductImageView)
            .load(mainDetailsForView.imageUrl)
            .into(ivProductImageView)
        Glide.with(ivProductHealthIcon)
            .load(healthCategoryIcon)
            .into(ivProductHealthIcon)
        cvProductHealthCategory.setCardBackgroundColor(healthCategoryBg)
        llProductDetailsLayout.addView(itemView)
    }

    private fun buildNutrientsView(
        nutrientCategory: NutrientCategory,
        productType: ProductType,
        nutrientsForView: List<Nutrient>){

        val headerView = LayoutInflater.from(requireContext()).inflate(R.layout.nutrients_header, llProductDetailsLayout, false)
        val tvNutrientHeader: TextView = headerView.findViewById(R.id.tv_nutrients_header)
        val tvServingQuantity: TextView = headerView.findViewById(R.id.serving_quantity)
        tvNutrientHeader.text = nutrientCategory.header
        tvServingQuantity.text = AppResources.getServingTextFromProductType(productType)
        llProductDetailsLayout.addView(headerView)
        for(nutrient in nutrientsForView){
            val (healthCategoryIcon, _) = getHealthCategoryIcon(
                requireContext(),nutrient.healthCategory
            )
            val nutrientView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_ingredient_item, llProductDetailsLayout, false)
            val tvNutrientName: TextView = nutrientView.findViewById(R.id.tv_nutrient_name)
            val tvNutrientDescription: TextView = nutrientView.findViewById(R.id.tv_nutrient_description)
            val tvNutrientPerHundredGram: TextView = nutrientView.findViewById(R.id.tv_per_hundred_gram)
            val ivNutrientIcon: ImageView = nutrientView.findViewById(R.id.nutrient_icon)
            val ivNutrientCategoryIcon: ImageView = nutrientView.findViewById(R.id.nutrient_category_icon)

            tvNutrientName.text = nutrient.nutrientType.heading
            tvNutrientDescription.text = nutrient.description
            tvNutrientPerHundredGram.text = nutrient.let { nutrient ->
                "${nutrient.contentPerHundredGrams} ${nutrient.servingUnit}"
            }
            Glide.with(ivNutrientIcon.context)
                .load(getNutrientIcon(nutrient.nutrientType))
                .into(ivNutrientIcon)
            Glide.with(ivNutrientCategoryIcon.context)
                .load(healthCategoryIcon)
                .into(ivNutrientCategoryIcon)
            llProductDetailsLayout.addView(nutrientView)
        }

    }
    private fun getHealthCategoryIcon(context : Context, healthCategory: HealthCategory) : Pair<Int, Int> {
        return when(healthCategory){
            HealthCategory.HEALTHY,
            HealthCategory.GOOD
            -> Pair(R.drawable.circle_good, ContextCompat.getColor(context, R.color.product_category_good_bg))
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

    private fun getNutrientIcon(nutrientType: NutrientType) : Int {
        return  when(nutrientType){
            NutrientType.ENERGY -> R.mipmap.calories
            NutrientType.PROTEIN -> R.mipmap.protein
            NutrientType.SATURATES -> R.mipmap.saturated_fat
            NutrientType.SUGAR -> R.mipmap.sugar
            NutrientType.FIBRE -> R.mipmap.fibre
            NutrientType.SODIUM -> R.mipmap.salt
            NutrientType.FRUITS_VEGETABLES_AND_NUTS -> R.mipmap.fruits_veggies
        }
    }
}
