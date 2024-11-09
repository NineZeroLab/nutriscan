package com.mdev.feature_profile.presentation.profilePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.common.utils.domain.model.Status
import com.mdev.core.utils.addImage
import com.mdev.core.utils.getInput
import com.mdev.core.utils.hide
import com.mdev.core.utils.hideKeyBoard
import com.mdev.core.utils.isValidUserName
import com.mdev.core.utils.isVisible
import com.mdev.core.utils.logger
import com.mdev.core.utils.show
import com.mdev.core.utils.showSnackBar
import com.mdev.feature_profile.R
import com.mdev.feature_profile.databinding.FragmentProfilePageBinding
import com.mdev.feature_profile.navigation.ProfileNavigator
import com.mdev.openfoodfacts_client.domain.model.Allergen
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction.PALM_OIL_FREE
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction.VEGAN
import com.mdev.openfoodfacts_client.domain.model.DietaryRestriction.VEGETARIAN
import com.mdev.openfoodfacts_client.domain.model.NutrientPreference
import com.mdev.openfoodfacts_client.domain.model.NutrientPreferenceType
import com.mdev.openfoodfacts_client.domain.model.NutrientType
import com.mdev.openfoodfacts_client.utils.ClientResources.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mdev.common.R as CommonRes

@AndroidEntryPoint
class ProfilePage : Fragment() {
    private lateinit var dietaryPreferences: MutableList<NutrientPreference>
    private lateinit var dietaryRestrictions: MutableList<DietaryRestriction>
    private lateinit var allergens: MutableList<Allergen>
    private lateinit var viewBinding: FragmentProfilePageBinding
    private lateinit var viewModel: ProfilePageViewModel
    @Inject
    lateinit var navigator: ProfileNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfilePageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ProfilePageViewModel::class.java]
        return viewBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUiState(view)
        if (viewModel.uiState.value.appUser == null){
            logger("app user is empty fetching user details from firebase")
            viewModel.onEvent(ProfilePageEvent.GetUserProfileDetails)
        }else{
            logger("app user already in viewModel.. building ui")
            buildUi()
        }
   }

    private fun buildUi() {
        buildToolbar()
        prefillUserName()
        updateUserPreferences()
        handleDietaryPreferenceCollapsableButton()
        handleDietaryRestrictionCollapsableButton()
        handleAllergenCollapsableButton()
        buildDietaryPreferenceView()
        buildDietaryRestrictionView()
        buildAllergenView()
        view?.let { handleSaveButton(it) }

    }


    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->
                    when(state.getProfileDetailsStatus){
                        Status.LOADING -> {

                        }
                        Status.SUCCESS -> {
                            buildUi()
                        }
                        Status.FAILURE -> {

                        }
                        Status.IDLE -> {

                        }
                    }
                    logger(state.profileUpdateStatus.name)
                    when(state.profileUpdateStatus){
                        Status.LOADING -> {
                            logger("loading in fragment for profile update status")
                        }
                        Status.SUCCESS -> {
                            view.showSnackBar("Updated Profile Details Successfully")
                            navigator.navigateFromProfileToHomePage(view.findFragment())
                        }
                        Status.FAILURE -> {
                            view.showSnackBar(state.errorMessage.toString())
                        }
                        Status.IDLE -> {}
                    }
                }
            }
        }

    }

    private fun handleSaveButton(view: View) {
        viewBinding.btnSaveUserDetails.setOnClickListener {
            val userName = viewBinding.tifUsername.getInput()
            val uid = viewModel.uiState.value.appUser?.uid ?: ""
            if (userName.isValidUserName()) {
                Log.d("logger", "Saving user details to viewModel $dietaryPreferences")
                val appUser = AppUser(
                    name = userName,
                    uid = uid,
                    profileUpdated = true,
                    dietaryPreferences = dietaryPreferences,
                    dietaryRestrictions = dietaryRestrictions,
                    allergens = allergens
                )
                viewModel.onEvent(ProfilePageEvent.UpdateProfileDetails(appUser))
            }else {
                view.showSnackBar("Invalid UserName")
            }
        }
    }

    private fun buildAllergenView() {
        for (allergen in Allergen.entries) {
            val button = ToggleButton(requireContext())
            button.apply {
                background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.dietary_restriction_selector
                )
                textOn = allergen.heading
                textOff = allergen.heading
                text = allergen.heading
                isChecked = allergen in allergens
                transformationMethod = null
                if (isChecked) {
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            CommonRes.color.md_theme_onPrimary
                        )
                    )
                } else {
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            CommonRes.color.md_theme_onSurface
                        )
                    )
                }

                setPadding(12, 0, 12, 0)
                id = View.generateViewId()
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        allergens.add(allergen)
                        buttonView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                CommonRes.color.md_theme_onPrimary
                            )
                        )

                    } else {
                        allergens.remove(allergen)
                        buttonView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                CommonRes.color.md_theme_onSurface
                            )
                        )
                    }
                }
            }
            val buttonIds = viewBinding.flAllergenLayout.referencedIds.toMutableList()
            buttonIds.add(button.id)
            viewBinding.flAllergenLayout.referencedIds = buttonIds.toIntArray()

            Log.d("logger", "adding ${allergen.heading} button to layout")
            viewBinding.clAllergenCollapsable.addView(button)
        }
    }

    private fun buildDietaryRestrictionView() {
        val restrictions = mutableListOf(
            VEGAN,
            VEGETARIAN,
            PALM_OIL_FREE
        )
        restrictions.forEach { restriction ->
            val button = ToggleButton(requireContext())
            button.apply {
                background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.dietary_restriction_selector
                )
                textOn = restriction.heading
                textOff = restriction.heading
                text = restriction.heading
                isChecked = restriction in dietaryRestrictions
                transformationMethod = null
                if (isChecked) {
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            CommonRes.color.md_theme_onPrimary
                        )
                    )
                } else {
                    setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            CommonRes.color.md_theme_onSurface
                        )
                    )
                }
                setPadding(12, 0, 12, 0)
                id = View.generateViewId()
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        buttonView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                CommonRes.color.md_theme_onPrimary
                            )
                        )
                        dietaryRestrictions.add(restriction)

                    } else {
                        dietaryRestrictions.remove(restriction)
                        buttonView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                CommonRes.color.md_theme_onSurface
                            )
                        )
                    }
                }
    //                background = ContextCompat.getDrawable(requireContext(),R.color.md_theme_primary)
            }
            val buttonIds = viewBinding.flDietaryRestrictionCollapsableLayout.referencedIds.toMutableList()
            buttonIds.add(button.id)
            viewBinding.flDietaryRestrictionCollapsableLayout.referencedIds = buttonIds.toIntArray()
            Log.d("logger", "adding ${restriction.heading} button to layout")
            viewBinding.clCollapsableDietaryRestriction.addView(button)
        }
    }

    private fun handleAllergenCollapsableButton() {
        viewBinding.llAllergenHeaderLayout.setOnClickListener {
            it.hideKeyBoard()
            viewBinding.apply {
                expandLayout(clAllergenCollapsable, ivAllergenExpandCollapse)
            }
        }
    }

    private fun handleDietaryRestrictionCollapsableButton() {
        viewBinding.llDietaryRestrictionHeader.setOnClickListener {
            it.hideKeyBoard()
            viewBinding.apply {
                expandLayout(clCollapsableDietaryRestriction, ivDietaryRestrictionExpandCollapse)
            }
        }
    }

    private fun handleDietaryPreferenceCollapsableButton() {
        viewBinding.llDietaryPrefereneHeader.setOnClickListener {
            it.hideKeyBoard()
            viewBinding.apply {
                expandLayout(clDietaryPreferenceCollapsable, ivDietaryPreferenceExpandCollapse)
            }
        }
    }

    private fun buildDietaryPreferenceView() {
        for (i in 0..<dietaryPreferences.size) {
            val nutrient = dietaryPreferences[i]
            val itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.nutrient_radio_group, viewBinding.nutrientsCollapsableLayout, false)
            val tvNutrientName: TextView = itemView.findViewById(R.id.tv_rg_nutrient_name)
            val rgNutrientPreference: RadioGroup =
                itemView.findViewById(R.id.rg_nutrient_preference)
            val rbNutrientLow: RadioButton = itemView.findViewById(R.id.rb_nutrient_low)
            val rbNutrientModerate: RadioButton = itemView.findViewById(R.id.rb_nutrient_moderate)
            val rbNutrientHigh: RadioButton = itemView.findViewById(R.id.rb_nutrient_high)
            tvNutrientName.text = nutrient.nutrientType?.heading
            rbNutrientLow.isChecked = nutrient.nutrientPreferenceType == NutrientPreferenceType.LOW
            rbNutrientModerate.isChecked =
                nutrient.nutrientPreferenceType == NutrientPreferenceType.MODERATE
            rbNutrientHigh.isChecked =
                nutrient.nutrientPreferenceType == NutrientPreferenceType.HIGH

            rbNutrientLow.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dietaryPreferences[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.LOW
                    )
                }
            }
            rbNutrientModerate.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dietaryPreferences[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.MODERATE
                    )
                }
            }
            rbNutrientHigh.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dietaryPreferences[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.HIGH
                    )
                }
            }
            tvNutrientName.setOnLongClickListener {
                rgNutrientPreference.clearCheck()
                dietaryPreferences[i] = nutrient.copy(
                    nutrientPreferenceType = null
                )
                true
            }
            viewBinding.nutrientsCollapsableLayout.addView(itemView)
        }
    }

    private fun updateUserPreferences() {
        val appUser = viewModel.uiState.value.appUser
        if (appUser?.profileUpdated == true) {
            Log.d(TAG, "Profile detailed was updated earlier")
            Log.d(TAG, "Prefilling existing values...")
            dietaryPreferences = appUser.dietaryPreferences.toMutableList()
            dietaryRestrictions = appUser.dietaryRestrictions.toMutableList()
            allergens = appUser.allergens.toMutableList()
        } else {
            Log.d(TAG, "Profile detailed was not updated earlier")
            dietaryPreferences = mutableListOf()
            dietaryRestrictions = mutableListOf()
            allergens = mutableListOf()
            NutrientType.entries.forEach { nutrientType ->
                dietaryPreferences.add(
                    element = NutrientPreference(
                        nutrientType = nutrientType,
                        nutrientPreferenceType = null
                    )
                )
            }
        }
    }

    private fun prefillUserName() {
        viewBinding.tifUsername.editText?.setText(viewModel.uiState.value.appUser?.name)
    }

    private fun buildToolbar() {
        viewBinding.mtbProfilePage.apply {
            addMenuProvider(
                object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.profile_page_menu, menu)
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        return when (menuItem.itemId) {
                            R.id.mi_logout_profile -> {
                                viewModel.onEvent(ProfilePageEvent.LogOut)
                                navigator.logout(this@ProfilePage)
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    }
                }, viewLifecycleOwner
            )
            if (viewModel.uiState.value.appUser?.profileUpdated == true){
//                setupWithNavController(findNavController())
                navigationIcon = ContextCompat.getDrawable(requireContext(),CommonRes.drawable.baseline_arrow_back_24)
                setNavigationIconTint(ContextCompat.getColor(requireContext(),CommonRes.color.md_theme_onPrimary))
            }
            title = "Profile Page"
        }
    }

    private fun expandLayout(layout: ConstraintLayout, iconView: ImageView){
        val headers = listOf(
            Pair(viewBinding.clDietaryPreferenceCollapsable,viewBinding.ivDietaryPreferenceExpandCollapse),
            Pair(viewBinding.clCollapsableDietaryRestriction,viewBinding.ivDietaryRestrictionExpandCollapse),
            Pair(viewBinding.clAllergenCollapsable,viewBinding.ivAllergenExpandCollapse)
        )
        if (layout.isVisible()){
            logger("$layout is visible hiding layout..")
            layout.hide()
            iconView.addImage(R.mipmap.arrow_down)
            return
        }
        for((header, imageView) in headers){
            logger("checking $header is visible: ${header.isVisible()}")
            if (header == layout){
                logger("$layout is gone expanding layout..")
                header.show()
                imageView.addImage(R.mipmap.arrow_up)
            }else{
                header.hide()
                imageView.addImage(R.mipmap.arrow_down)
            }
        }
    }
}