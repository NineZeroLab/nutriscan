package com.zero1labs.nutriscan.pages.profilePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.models.data.AppUser
import com.zero1labs.nutriscan.models.data.NutrientPreference
import com.zero1labs.nutriscan.models.data.NutrientPreferenceType
import com.zero1labs.nutriscan.pages.homepage.HomePageEvent
import com.zero1labs.nutriscan.pages.homepage.HomePageViewModel
import com.zero1labs.nutriscan.pages.homepage.UserDetailsUpdateState
import com.zero1labs.nutriscan.utils.Allergen
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.DietaryRestriction
import com.zero1labs.nutriscan.utils.NutrientType
import kotlinx.coroutines.launch

class WelcomePage : Fragment(R.layout.fragment_welcome_page) {


    private lateinit var clAllergenCollapsableLayout: ConstraintLayout
    private lateinit var clDietaryPreferenceCollapsable: ConstraintLayout
    private lateinit var clDietaryRestrictionCollapsable: ConstraintLayout
    private lateinit var ivDietaryPreferenceCollapsableIcon: ImageView
    private lateinit var ivDietaryRestrictionCollapsableIcon: ImageView
    private lateinit var ivAllergenCollapsableIcon: ImageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var dietaryPreferences: MutableList<NutrientPreference>
        lateinit var userName: String
        lateinit var dietaryRestrictions: MutableList<DietaryRestriction>
        lateinit var allergens: MutableList<Allergen>
        val viewModel = ViewModelProvider(requireActivity())[HomePageViewModel::class.java]
        val btnSaveUserData: Button = view.findViewById(R.id.btn_save_user_details)
        val mainView: View = view.findViewById(R.id.welcome_page_layout)
        val llNutrientPreferenceCollapsable: LinearLayout = view.findViewById(R.id.nutrients_collapsable_layout)
        clDietaryPreferenceCollapsable = view.findViewById(R.id.cl_dietary_preference_collapsable)
        clDietaryRestrictionCollapsable = view.findViewById(R.id.cl_collapsable_dietary_restriction)
        val flNutrientRestrictionCollapsableFlow: Flow = view.findViewById(R.id.ll_dietary_restriction_collapsable_layout)
        val tifUserName: TextInputLayout = view.findViewById(R.id.tif_username)
        ivDietaryPreferenceCollapsableIcon = view.findViewById(R.id.iv_dietary_preference_expand_collapse)
        ivDietaryRestrictionCollapsableIcon = view.findViewById(R.id.iv_dietary_restriction_expand_collapse)
        val llDietaryPreferenceHeader: LinearLayout = view.findViewById(R.id.ll_dietary_preferene_header)
        val llDietaryRestrictionHeader: LinearLayout = view.findViewById(R.id.ll_dietary_restriction_header)
        val llAllergenHeaderLayout: LinearLayout = view.findViewById(R.id.ll_allergen_header_layout)
        ivAllergenCollapsableIcon = view.findViewById(R.id.iv_allergen_expand_collapse)
        clAllergenCollapsableLayout = view.findViewById(R.id.cl_allergen_collapsable)
        val flAllergenCollapsableFlow: Flow = view.findViewById(R.id.fl_allergen_layout)
        val appUser = viewModel.uiState.value.appUser

        tifUserName.editText?.setText(appUser?.name)
        Log.d(TAG, "${appUser?.profileUpdated}")

        if (appUser?.profileUpdated == true){
            Log.d(TAG, "Profile detailed was updated earlier")
            Log.d(TAG, "Prefilling existing values...")
            dietaryPreferences = appUser.dietaryPreferences.toMutableList()
            dietaryRestrictions = appUser.dietaryRestrictions.toMutableList()
            allergens = appUser.allergens.toMutableList()
        }else{

            Log.d(TAG, "Profile detailed was not updated earlier")
            dietaryPreferences = mutableListOf<NutrientPreference>()
            dietaryRestrictions = mutableListOf<DietaryRestriction>()
            allergens = mutableListOf<Allergen>()
            NutrientType.values().forEach { nutrientType ->
                dietaryPreferences.add(element = NutrientPreference(
                    nutrientType = nutrientType,
                    nutrientPreferenceType = null
                ))
            }
        }




        for (i in 0..<dietaryPreferences.size){
            val nutrient = dietaryPreferences[i]
            val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.nutrient_radio_group,llNutrientPreferenceCollapsable,false)
            val tvNutrientName: TextView = itemView.findViewById(R.id.tv_rg_nutrient_name)
            val rgNutrientPreference: RadioGroup = itemView.findViewById(R.id.rg_nutrient_preference)
            val rbNutrientLow: RadioButton = itemView.findViewById(R.id.rb_nutrient_low)
            val rbNutrientModerate: RadioButton = itemView.findViewById(R.id.rb_nutrient_moderate)
            val rbNutrientHigh: RadioButton = itemView.findViewById(R.id.rb_nutrient_high)
            tvNutrientName.text = nutrient.nutrientType?.heading
            rbNutrientLow.isChecked = nutrient.nutrientPreferenceType == NutrientPreferenceType.LOW
            rbNutrientModerate.isChecked = nutrient.nutrientPreferenceType == NutrientPreferenceType.MODERATE
            rbNutrientHigh.isChecked = nutrient.nutrientPreferenceType == NutrientPreferenceType.HIGH

            rbNutrientLow.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    dietaryPreferences[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.LOW
                    )
                }
            }
            rbNutrientModerate.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked){
                    dietaryPreferences[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.MODERATE
                    )
                }
            }
            rbNutrientHigh.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked){
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



            llNutrientPreferenceCollapsable.addView(itemView)
        }

        llDietaryPreferenceHeader.setOnClickListener {
            expandLayout(clDietaryPreferenceCollapsable, ivDietaryPreferenceCollapsableIcon)
        }

        llDietaryRestrictionHeader.setOnClickListener {
            expandLayout(clDietaryRestrictionCollapsable, ivDietaryRestrictionCollapsableIcon)
        }
        llAllergenHeaderLayout.setOnClickListener{
            expandLayout(clAllergenCollapsableLayout, ivAllergenCollapsableIcon)
        }


        for (restriction in DietaryRestriction.values()){
            val button = ToggleButton(requireContext())
            button.apply {
                button.background = ContextCompat.getDrawable(requireContext(), R.drawable.dietary_restriction_selector)
                button.textOn = restriction.heading
                button.textOff = restriction.heading
                button.text = restriction.heading
                button.isChecked = restriction in dietaryRestrictions
                transformationMethod = null

                setPadding(12,0,12,0)
                id = View.generateViewId()
                setOnCheckedChangeListener { buttonView,isChecked ->
                    if (isChecked){
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onPrimary))
                        dietaryRestrictions.add(restriction)

                    }else{
                        dietaryRestrictions.remove(restriction)
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onSurface))
                    }
                }
//                background = ContextCompat.getDrawable(requireContext(),R.color.md_theme_primary)
            }
            val buttonIds = flNutrientRestrictionCollapsableFlow.referencedIds.toMutableList()
            buttonIds.add(button.id)
            flNutrientRestrictionCollapsableFlow.referencedIds = buttonIds.toIntArray()


            Log.d("logger", "adding ${restriction.heading} button to layout")
            clDietaryRestrictionCollapsable.addView(button)
        }


        for(allergen in Allergen.values()){
            val button = ToggleButton(requireContext())
            button.apply {
                background = ContextCompat.getDrawable(requireContext(), R.drawable.dietary_restriction_selector)
                textOn = allergen.heading
                textOff = allergen.heading
                text = allergen.heading
                isChecked = allergen in allergens
                transformationMethod = null

                setPadding(12,0,12,0)
                id = View.generateViewId()
                setOnCheckedChangeListener { buttonView,isChecked ->
                    if (isChecked){
                        allergens.add(allergen)
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onPrimary))

                    }else{
                        allergens.remove(allergen)
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onSurface))
                    }
                }
            }
            val buttonIds = flAllergenCollapsableFlow.referencedIds.toMutableList()
            buttonIds.add(button.id)
            flAllergenCollapsableFlow.referencedIds = buttonIds.toIntArray()

            Log.d("logger", "adding ${allergen.heading} button to layout")
            clAllergenCollapsableLayout.addView(button)
        }

          btnSaveUserData.setOnClickListener{
              val userName = tifUserName.editText?.text.toString()
              val uid = viewModel.uiState.value.appUser?.uid ?: ""
            if (validateUserData(mainView, userName, dietaryPreferences)){
                Log.d("logger", "Saving user details to viewModel s${dietaryPreferences.toString()}")
                val appUser = AppUser(
                    name = tifUserName.editText?.text.toString(),
                    uid = uid,
                    profileUpdated = true,
                    dietaryPreferences = dietaryPreferences,
                    dietaryRestrictions = dietaryRestrictions,
                    allergens = allergens
                )
               viewModel.onEvent(HomePageEvent.UpdateUserPreferences(appUser))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.userDetailsUpdateState){
                    UserDetailsUpdateState.LOADING -> {}
                    UserDetailsUpdateState.SUCCESS -> {
                        Snackbar.make(view,state.msg.toString(),Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack(R.id.homePage,false)
                    }
                    UserDetailsUpdateState.FAILURE -> {
                        Snackbar.make(view,state.msg.toString(),Snackbar.LENGTH_LONG).show()
                    }
                    UserDetailsUpdateState.NOT_STARTED -> {}
                }
            }
        }


   }
    private fun expandLayout(layout: ConstraintLayout, iconView: ImageView){
        val headers = mutableListOf(
            Pair(clDietaryRestrictionCollapsable,ivDietaryRestrictionCollapsableIcon),
            Pair(clDietaryPreferenceCollapsable,ivDietaryPreferenceCollapsableIcon),
            Pair(clAllergenCollapsableLayout,ivAllergenCollapsableIcon)
        )
        if (layout.visibility == View.VISIBLE){
            layout.visibility = View.GONE
            Glide.with(requireContext())
                .load(R.mipmap.arrow_down)
                .into(iconView)
            return
        }
        for((header, imageView) in headers){
            if (header == layout){
                header.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_up)
                    .into(imageView)
            }else{
                header.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(imageView)
            }
        }
    }

    private fun validateUserData(view: View, userName: String, nutrientPreference: List<NutrientPreference>): Boolean {
        if (userName == ""){
            showSnackBar(view,"Invalid Username")
            return false
        }
        nutrientPreference.forEach { nutrientPreference ->
            if (nutrientPreference.nutrientPreferenceType == null){
//                showSnackBar(view, message = "${nutrientPreference.nutrientType?.heading} is not selected")
//                return false
            }
        }
        return  true
    }

    private fun showSnackBar(view: View, message: String){
        val snackbar = Snackbar.make(view,message,Snackbar.ANIMATION_MODE_SLIDE)
            .setAction("X", View.OnClickListener {  })
        snackbar.show()
    }


    private fun createDietaryPreference(nutrientType: NutrientType):NutrientPreference {
        return NutrientPreference(
            nutrientType =nutrientType ,
            nutrientPreferenceType = null
    )
    }
}