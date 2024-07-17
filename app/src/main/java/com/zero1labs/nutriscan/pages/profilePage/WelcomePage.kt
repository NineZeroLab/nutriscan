package com.zero1labs.nutriscan.pages.profilePage

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
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
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.models.data.NutrientPreference
import com.zero1labs.nutriscan.models.data.NutrientPreferenceType
import com.zero1labs.nutriscan.utils.NutrientType
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel

class WelcomePage : Fragment(R.layout.fragment_welcome_page) {

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        val btnSaveUserData: Button = view.findViewById(R.id.btn_save_user_details)
        val mainView: View = view.findViewById(R.id.welcome_page_layout)
        val llNutrientPreferenceCollapsable: LinearLayout = view.findViewById(R.id.nutrients_collapsable_layout)
        val clDietaryPreferenceCollapsable: ConstraintLayout = view.findViewById(R.id.cl_dietary_preference_collapsable)
        val clNutrientRestrictionCollapsable: ConstraintLayout = view.findViewById(R.id.cl_collapsable_dietary_restriction)
        val flNutrientRestrictionCollapsable: Flow = view.findViewById(R.id.ll_dietary_restriction_collapsable_layout)
        val tvDietaryPreferenceHeader: TextView = view.findViewById(R.id.tv_dietary_preference_header)
        val tvDietaryRestrictionHeader: TextView = view.findViewById(R.id.tv_dietary_restriction_header)
        val tiUserNameText: TextInputEditText = view.findViewById(R.id.ti_username_edit_text)
        val ivDietaryPreferenceCollapsableIcon: ImageView = view.findViewById(R.id.iv_dietary_preference_expand_collapse)
        val ivDietaryRestrictionCollapsableIcon: ImageView = view.findViewById(R.id.iv_dietary_restriction_expand_collapse)
        val llDietaryPreferenceHeader: LinearLayout = view.findViewById(R.id.ll_dietary_preferene_header)
        val llDietaryRestrictionHeader: LinearLayout = view.findViewById(R.id.ll_dietary_restriction_header)
        var userName = ""

        tiUserNameText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(username: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userName = username.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        val nutrientPreference = mutableListOf<NutrientPreference>()
        NutrientType.values().forEach { nutrientType ->
            nutrientPreference.add(element = NutrientPreference(
                nutrientType = nutrientType,
                nutrientPreferenceType = null
            ))
        }


        for (i in 0..<nutrientPreference.size){
            val nutrient = nutrientPreference[i]
            val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.nutrient_radio_group,llNutrientPreferenceCollapsable,false)
            val tvNutrientName: TextView = itemView.findViewById(R.id.tv_rg_nutrient_name)
            val rgNutrientPreference: RadioGroup = itemView.findViewById(R.id.rg_nutrient_preference)
            val rbNutrientLow: RadioButton = itemView.findViewById(R.id.rb_nutrient_low)
            val rbNutrientModerate: RadioButton = itemView.findViewById(R.id.rb_nutrient_moderate)
            val rbNutrientHigh: RadioButton = itemView.findViewById(R.id.rb_nutrient_high)
            tvNutrientName.text = nutrient.nutrientType.heading

            rbNutrientLow.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    nutrientPreference[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.LOW
                    )
                }
            }
            rbNutrientModerate.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked){
                    nutrientPreference[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.MODERATE
                    )
                }
            }
            rbNutrientHigh.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked){
                    nutrientPreference[i] = nutrient.copy(
                        nutrientPreferenceType = NutrientPreferenceType.HIGH
                    )
                }
            }



            llNutrientPreferenceCollapsable.addView(itemView)
        }

        //TODO: make the whole linear layout clickable
        llDietaryPreferenceHeader.setOnClickListener {
            if (clDietaryPreferenceCollapsable.visibility == View.VISIBLE){
                clDietaryPreferenceCollapsable.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryPreferenceCollapsableIcon)
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryRestrictionCollapsableIcon)
            }else{
                clDietaryPreferenceCollapsable.visibility = View.VISIBLE
                clNutrientRestrictionCollapsable.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_up)
                    .into(ivDietaryPreferenceCollapsableIcon)
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryRestrictionCollapsableIcon)
            }
        }

        //TODO: make the whole linear layout clickable
        llDietaryRestrictionHeader.setOnClickListener {
            if (clNutrientRestrictionCollapsable.visibility == View.VISIBLE){
                clNutrientRestrictionCollapsable.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryPreferenceCollapsableIcon)
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryRestrictionCollapsableIcon)
            }else{
                clNutrientRestrictionCollapsable.visibility = View.VISIBLE
                clDietaryPreferenceCollapsable.visibility = View.GONE
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_down)
                    .into(ivDietaryPreferenceCollapsableIcon)
                Glide.with(requireContext())
                    .load(R.mipmap.arrow_up)
                    .into(ivDietaryRestrictionCollapsableIcon)
            }
        }

        flNutrientRestrictionCollapsable.setPadding(4)
        flNutrientRestrictionCollapsable.setVerticalGap(32)

        for (i in 0..<nutrientPreference.size){
            val nutrient = nutrientPreference[i]
            val button = ToggleButton(requireContext())


            button.apply {


                button.background = ContextCompat.getDrawable(requireContext(), R.drawable.dietary_restriction_selector)
                button.textOn = nutrient.nutrientType.heading
                button.textOff = nutrient.nutrientType.heading
                button.text = nutrient.nutrientType.heading
                button.isChecked = false
                transformationMethod = null

                setPadding(12,0,12,0)
                id = View.generateViewId()
                setOnCheckedChangeListener { buttonView,isChecked ->
                    if (isChecked){
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onError))
                    }else{
                        buttonView.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_onSurface))
                    }
                }
//                background = ContextCompat.getDrawable(requireContext(),R.color.md_theme_primary)
            }
            val buttonIds = flNutrientRestrictionCollapsable.referencedIds.toMutableList()
            buttonIds.add(button.id)
            flNutrientRestrictionCollapsable.referencedIds = buttonIds.toIntArray()



            Log.d("logger", "adding ${nutrient.nutrientType.heading} button to layout")
            clNutrientRestrictionCollapsable.addView(button)
        }

          btnSaveUserData.setOnClickListener{
            if (validateUserData(mainView, userName, nutrientPreference)){
                Log.d("logger", "Saving user details to viewModel s${nutrientPreference.toString()}")
               viewModel.onEvent(AppEvent.SaveUserData(userName = userName, nutrientPreference))
                findNavController().navigate(R.id.action_welcome_page_to_home_page)
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
                showSnackBar(view, message = "${nutrientPreference.nutrientType.heading} is not selected")
                return false
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