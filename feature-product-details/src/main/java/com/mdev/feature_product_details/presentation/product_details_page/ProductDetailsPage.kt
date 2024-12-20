package com.mdev.feature_product_details.presentation.product_details_page

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.addImage
import com.mdev.common.R as CommonRes
import com.mdev.feature_product_details.R
import com.mdev.openfoodfacts_client.domain.model.NutrientCategory
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.HealthCategory
import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.domain.model.ProductType
import com.mdev.core.utils.hide
import com.mdev.core.utils.logger
import com.mdev.core.utils.showSnackBar
import com.mdev.feature_product_details.databinding.FragmentProductDetailsPageBinding
import com.mdev.feature_product_details.domain.model.AdditivesShortView
import com.mdev.feature_product_details.domain.model.MainDetailsForView
import com.mdev.feature_product_details.domain.model.Nutrient
import com.mdev.feature_product_details.domain.model.RecommendedProduct
import com.mdev.feature_product_details.navigation.ProductDetailsNavigator
import com.mdev.openfoodfacts_client.utils.ClientResources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsPage : Fragment() {

    private lateinit var llProductDetailsLayout: LinearLayout
    private lateinit var viewBinding: FragmentProductDetailsPageBinding
    private lateinit var viewModel: ProductDetailsViewModel
    @Inject
    lateinit var navigator: ProductDetailsNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[ProductDetailsViewModel::class.java]
        viewBinding = FragmentProductDetailsPageBinding.inflate(inflater, container,false)
        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleArguments()
        buildInitialToolbar()
        llProductDetailsLayout = viewBinding.llProductDetailsLayout
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ state ->
                    when(state.productDetailsFetchState){
                        Status.LOADING -> {
                            logger("Loading product details...")
                        }
                        Status.SUCCESS -> {
                            logger("Loading product success...")
                            hideProgressBar()
//                            updateToolbar()
                            buildUi()
                        }
                        Status.FAILURE -> {
                            logger("Loading product failure...")
                            hideProgressBar()
                            view.showSnackBar(state.errorMessage.toString())
                        }
                        Status.IDLE -> {}
                    }
                    when(state.recommendedProductsFetchState){
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            logger(state.recommendedProducts.toString())
                            buildRecommendedProducts(state.recommendedProducts)
                        }
                        Status.FAILURE -> {
                            view.showSnackBar("Unable to fetch recommended products!")
                        }
                        Status.IDLE -> {

                        }
                    }
                }
            }
        }
    }

    private fun handleArguments() {
        val args = arguments
        val productId = args?.getString("productId")
        if (productId.isNullOrEmpty()){
            viewModel.onEvent(ProductDetailsPageEvent.GetProductDetails(ClientResources.getRandomItem()))
        }else{
            viewModel.onEvent(ProductDetailsPageEvent.GetProductDetails(productId.toString()))
        }
        logger("Fetching details for product $productId")
    }

    private fun buildInitialToolbar() {
        viewBinding.mtbDetailsPage.title = "Loading Details..."
    }
    private fun updateToolbar(){
        viewBinding.mtbDetailsPage.apply {
            setupWithNavController(this.findNavController())
            setNavigationIcon(CommonRes.drawable.baseline_arrow_back_24)
            setNavigationIconTint(ContextCompat.getColor(requireContext(),CommonRes.color.md_theme_onPrimary))
            title = "Product Details"
        }
    }

    private fun buildUi() {
        llProductDetailsLayout.removeAllViews()
        viewModel.uiState.value.let { state ->
            state.productDetails?.let { productDetails ->

                buildMainHeader( productDetails.mainDetailsForView, state.userConclusion?.dietaryPreferenceConclusion ?: "")
            }
            if (state.productConsiderations != null && state.userConsiderations != null){
                buildAllergensView(
                    productAllergen = state.productConsiderations.allergens,
                    userAllergens = state.userConsiderations.allergens
                )
            }
            state.userConclusion?.let { userConclusion ->
                if (userConclusion.dietaryRestrictionConclusion != "" || userConclusion.allergenConclusion != ""){
                    buildFoodConsiderations(userConclusion.dietaryRestrictionConclusion,userConclusion.allergenConclusion)
                }
            }
            state.productDetails?.let {
                if(it.negativeNutrients.isNotEmpty()){
                    buildNutrientsView(NutrientCategory.NEGATIVE, it.productType, it.negativeNutrients)
                }
                if (it.positiveNutrients.isNotEmpty()){
                    buildNutrientsView(NutrientCategory.POSITIVE, it.productType, it.positiveNutrients)
                }
                if (it.additives.isNotEmpty()){
                    buildAdditives(it.additives)
                }
            }


        }
    }

    private fun buildFoodConsiderations(dietaryRestrictionConclusion: String, allergenConclusion: String) {
        if (dietaryRestrictionConclusion == "" && allergenConclusion == "") return
        val headerView = LayoutInflater.from(requireContext()).inflate(R.layout.nutrients_header,llProductDetailsLayout, false)
        headerView.findViewById<TextView>(R.id.tv_nutrients_header).text = getString(CommonRes.string.food_considerations)
        headerView.findViewById<TextView>(R.id.serving_quantity).text = ""
        llProductDetailsLayout.addView(headerView)
        if (dietaryRestrictionConclusion != ""){
            val restrictionConclusionView = LayoutInflater.from(requireContext()).inflate(R.layout.food_considerations_text, llProductDetailsLayout, false)
            restrictionConclusionView.findViewById<TextView>(R.id.tv_conclusion).text = dietaryRestrictionConclusion
            llProductDetailsLayout.addView(restrictionConclusionView)
        }
        if (allergenConclusion != ""){
            val allergenConclusionView = LayoutInflater.from(requireContext()).inflate(R.layout.food_considerations_text, llProductDetailsLayout, false)
            allergenConclusionView.findViewById<TextView>(R.id.tv_conclusion).text = allergenConclusion
            llProductDetailsLayout.addView(allergenConclusionView)
        }
    }

    private fun buildAllergensView(productAllergen: List<Allergen>, userAllergens: List<Allergen>) {
        if (productAllergen.isEmpty()){
            return
        }
        logger("Product allergens : ${productAllergen.toString()}")
        val allergensView = LayoutInflater.from(requireContext()).inflate(R.layout.component_food_considerations, llProductDetailsLayout, false)
        val clAllergensLayout = allergensView.findViewById<ConstraintLayout>(R.id.cl_allergens_layout)
        val flAllergensLayout = allergensView.findViewById<Flow>(R.id.fl_allergens_layout)
        productAllergen.forEach { allergen ->
            val button = ToggleButton(requireContext())
            button.apply {
                background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.allergen_selector
                )
                isAllCaps = false
                textOn = allergen.heading
                textOff = allergen.heading
                text = allergen.heading
                isChecked = allergen in userAllergens
                isClickable = false
                id = View.generateViewId()
                }
            button.setTextColor(
                resources.getColor(
                    if (button.isChecked) CommonRes.color.md_theme_onPrimary else CommonRes.color.md_theme_onBackground
                )
            )
            val buttonIds = flAllergensLayout.referencedIds.toMutableList()
            buttonIds.add(button.id)
            flAllergensLayout.referencedIds = buttonIds.toIntArray()
            clAllergensLayout.addView(button)
            }
        llProductDetailsLayout.addView(allergensView)
        }

    private fun buildMainHeader(
        mainDetailsForView: MainDetailsForView,
        dietaryPreferenceConclusion: String,
    ){
        val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.product_details_main_header, llProductDetailsLayout,false)
        val cvProductHealthCategory: CardView = itemView.findViewById(R.id.cv_product_health)
        val ivProductImageView: ImageView = itemView.findViewById(R.id.iv_product_image)
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvProductBrand: TextView = itemView.findViewById(R.id.tv_product_brand)
        val ivProductHealthIcon: ImageView = itemView.findViewById(R.id.iv_product_health_icon)
        val tvProductHealthGrade: TextView = itemView.findViewById(R.id.tv_product_health_grade)

        val (healthCategoryIcon, healthCategoryBg) = getHealthCategoryIcon(
            requireContext(),mainDetailsForView.healthCategory
        )
        tvProductName.text = mainDetailsForView.productName
        tvProductBrand.text = mainDetailsForView.productBrand
        tvProductHealthGrade.text = mainDetailsForView.healthCategory.description
        Log.d("logger", mainDetailsForView.imageUrl ?: "null")
        val imageUrl = if (mainDetailsForView.imageUrl == "") null else mainDetailsForView.imageUrl

        Glide.with(ivProductImageView)
            .load(imageUrl ?: CommonRes.mipmap.app_icon)
            .error(CommonRes.mipmap.app_icon)
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
        tvServingQuantity.text = ClientResources.getServingTextFromProductType(productType)
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
            tvNutrientPerHundredGram.text = nutrient.let {
                "${it.contentPerHundredGrams} ${it.servingUnit}"
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
    /**
     * To be moved to utils/KotlinExtensions
     */
    private fun getHealthCategoryIcon(context : Context, healthCategory: HealthCategory) : Pair<Int, Int> {
        return when(healthCategory){
            HealthCategory.HEALTHY,
            HealthCategory.GOOD
            -> Pair(CommonRes.drawable.circle_good, ContextCompat.getColor(context, CommonRes.color.product_category_good_bg))
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
                )
            )
        }
    }

    private fun buildAdditives(additives: List<AdditivesShortView>){
        val additivesHeaderView = LayoutInflater.from(requireContext()).inflate(R.layout.component_additives_header,llProductDetailsLayout, false)
        additivesHeaderView.findViewById<TextView>(R.id.tv_additives_nos).text = additives.size.toString()
        val additivesLevelContent = additivesHeaderView.findViewById<LinearLayout>(R.id.ll_additives_level_content)
        val dropDownImage = additivesHeaderView.findViewById<ImageView>(R.id.iv_drop_down_arrow)
        dropDownImage.setOnClickListener {
            if (additivesLevelContent.visibility == View.VISIBLE){
                additivesLevelContent.visibility = View.GONE
                dropDownImage.addImage(R.mipmap.arrow_down)
            }else{
                additivesLevelContent.visibility = View.VISIBLE
                dropDownImage.addImage(R.mipmap.arrow_up)
            }
        }

        additives.forEach { additive ->
            val additivesContentView = LayoutInflater.from(requireContext()).inflate(R.layout.component_additives_content_level, additivesLevelContent, false)
            additivesContentView.apply {
                findViewById<TextView>(R.id.tv_risk_level).text = additive.additiveRiskLevel.displayText
                findViewById<ImageView>(R.id.iv_risk_level).addImage(additive.additiveRiskLevel.icon)
                findViewById<TextView>(R.id.tv_risk_level_nos).text = additive.count.toString()
                findViewById<TextView>(R.id.tv_risk_level_nos).text = additive.count.toString()
            }
            additivesLevelContent.addView(additivesContentView)
        }
        llProductDetailsLayout.addView(additivesHeaderView)
    }

    private fun buildRecommendedProducts(recommendedProducts: List<RecommendedProduct>){
        recommendedProducts.forEach {
            logger("adding ${it.name} to recommended product list")
        }
        viewBinding.llRecommendedProducts.visibility = View.VISIBLE
        viewBinding.rvRecommendedProducts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBinding.rvRecommendedProducts.adapter = RecommendedProductsAdapter(recommendedProducts){ productId ->
            navigator.reloadWithNewProduct(this@ProductDetailsPage, productId)
        }
    }

    /**
     * To be moved to utils/KotlinExtensions
     */
    private fun getNutrientIcon(nutrientType: NutrientType) : Int {
        return  when(nutrientType){
            NutrientType.ENERGY -> CommonRes.mipmap.calories
            NutrientType.PROTEIN -> CommonRes.mipmap.protein
            NutrientType.SATURATES -> CommonRes.mipmap.saturated_fat
            NutrientType.SUGAR -> CommonRes.mipmap.sugar
            NutrientType.FIBRE -> CommonRes.mipmap.fibre
            NutrientType.SODIUM -> CommonRes.mipmap.salt
            NutrientType.FRUITS_VEGETABLES_AND_NUTS -> CommonRes.mipmap.fruits_veggies
        }
    }

    private fun showProgressBar(){
        logger("showing progressbar in details page")
        viewBinding.clPdpProgressbarLayout.visibility = View.VISIBLE
    }
    private fun hideProgressBar(){
        logger("hiding progressbar in details page")
        viewBinding.clPdpProgressbarLayout.hide()
    }

}
