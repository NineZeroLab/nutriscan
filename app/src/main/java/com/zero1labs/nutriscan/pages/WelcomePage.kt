package com.zero1labs.nutriscan.pages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        val btnSaveUserData: Button = view.findViewById(R.id.btn_save_user_details)
        val mainView: View = view.findViewById(R.id.welcome_page_layout)
        val llNutrientPreferenceCollapsable: LinearLayout = view.findViewById(R.id.nutrients_collapsable_layout)
        val clNutrientRestrictionCollapsable: ConstraintLayout = view.findViewById(R.id.cl_collapsable_dietary_restriction)
        val flNutrientRestrictionCollapsable: Flow = view.findViewById(R.id.ll_dietary_restriction_collapsable_layout)
        val tvDietaryPreferenceHeader: TextView = view.findViewById(R.id.tv_dietary_preference_header)
        val tvDietaryRestrictionHeader: TextView = view.findViewById(R.id.tv_dietary_restriction_header)
        val tiUserNameText: TextInputEditText = view.findViewById(R.id.ti_username_edit_text)
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

        tvDietaryPreferenceHeader.setOnClickListener {
            if (llNutrientPreferenceCollapsable.visibility == View.VISIBLE){
                llNutrientPreferenceCollapsable.visibility = View.GONE

            }else{
                    llNutrientPreferenceCollapsable.visibility = View.VISIBLE

            }
        }

        tvDietaryRestrictionHeader.setOnClickListener {
            if (clNutrientRestrictionCollapsable.visibility == View.VISIBLE){
                clNutrientRestrictionCollapsable.visibility = View.GONE
            }else{
                clNutrientRestrictionCollapsable.visibility = View.VISIBLE
            }
        }


        for (i in 0..<nutrientPreference.size){
            val nutrient = nutrientPreference[i]
            val button = MaterialButton(requireContext())

            button.apply {
                text = nutrient.nutrientType.heading
                id = View.generateViewId()
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
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