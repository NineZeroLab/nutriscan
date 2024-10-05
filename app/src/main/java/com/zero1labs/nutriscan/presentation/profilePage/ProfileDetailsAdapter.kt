package com.zero1labs.nutriscan.presentation.profilePage

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.domain.model.NutrientPreferenceType
import com.zero1labs.nutriscan.domain.model.ProfileDetailsListItems

class ProfileDetailsAdapter(private val profileDetailsListItems: MutableList<ProfileDetailsListItems>) : RecyclerView.Adapter<ViewHolder>() {

    companion object{
        private const val VIEW_TYPE_USERNAME_TEXT_INPUT_FIELD = 1
        private const val VIEW_TYPE_CONTENT_HEADER = 2
        private const val VIEW_TYPE_NUTRIENTS_PREFERENCE = 3

    }
    class UserNameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tifUserName: TextInputLayout = itemView.findViewById(R.id.tif_username)
        val tiUserNameText: TextInputEditText = itemView.findViewById(R.id.ti_username_text_input_text)

    }
    class ContentHeaderViewHolder(itemView: View): ViewHolder(itemView){
        val tvContentHeader: TextView = itemView.findViewById(R.id.tv_content_header)
    }

    class NutrientPreferenceViewHolder(itemView: View): ViewHolder(itemView){
        val tvNutrientName: TextView = itemView.findViewById(R.id.tv_rg_nutrient_name)
        val rgNutrientPreference: RadioGroup = itemView.findViewById(R.id.rg_nutrient_preference)
        val rbNutrientLow: RadioButton = itemView.findViewById(R.id.rb_nutrient_low)
        val rbNutrientModerate: RadioButton = itemView.findViewById(R.id.rb_nutrient_moderate)
        val rbNutrientHigh: RadioButton = itemView.findViewById(R.id.rb_nutrient_high)
    }

    override fun getItemViewType(position: Int): Int {
        return when(profileDetailsListItems[position]){
            is ProfileDetailsListItems.UserName -> VIEW_TYPE_USERNAME_TEXT_INPUT_FIELD
            is ProfileDetailsListItems.ContentHeader -> VIEW_TYPE_CONTENT_HEADER
            is ProfileDetailsListItems.DietaryPreferences -> VIEW_TYPE_NUTRIENTS_PREFERENCE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_USERNAME_TEXT_INPUT_FIELD -> {
                val view = inflater.inflate(R.layout.profile_username_input, parent, false)
                UserNameViewHolder(view)
            }

            VIEW_TYPE_CONTENT_HEADER -> {
                val view = inflater.inflate(R.layout.content_header, parent, false)
                ContentHeaderViewHolder(view)
            }

            VIEW_TYPE_NUTRIENTS_PREFERENCE -> {
                val view = inflater.inflate(R.layout.nutrient_radio_group, parent, false)
                NutrientPreferenceViewHolder(view)
            }

            else -> {throw IllegalArgumentException("Invalid View Type")}
        }
    }

    override fun getItemCount(): Int {
        return profileDetailsListItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(val item = profileDetailsListItems[position]){
            is ProfileDetailsListItems.ContentHeader -> {
                (holder as ContentHeaderViewHolder).apply {
                    tvContentHeader.text = item.heading
                    tvContentHeader.setOnClickListener {
                        holder.itemViewType
                    }
                }
            }
            is ProfileDetailsListItems.DietaryPreferences -> {
                (holder as NutrientPreferenceViewHolder).apply {
                    tvNutrientName.text = item.nutrientPreference.nutrientType?.heading
                    rbNutrientLow.setOnCheckedChangeListener{ _, isChecked ->
                        if (isChecked){
                           updateNutrientPreference(position, NutrientPreferenceType.LOW)
                        }
                    }
                    rbNutrientModerate.setOnCheckedChangeListener{ _, isChecked ->
                        if (isChecked){
                            updateNutrientPreference(position, NutrientPreferenceType.MODERATE)
                        }
                    }
                    rbNutrientHigh.setOnCheckedChangeListener{_, isChecked ->
                        if (isChecked){
                           updateNutrientPreference(position, NutrientPreferenceType.HIGH)
                        }
                    }
                }
            }
            is ProfileDetailsListItems.UserName -> {
                (holder as UserNameViewHolder).apply {
                    tifUserName.hint = "Username"
                    tiUserNameText.setText(item.userName)

                    tiUserNameText.addTextChangedListener(object : TextWatcher{
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(username: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            profileDetailsListItems[holder.adapterPosition] = item.copy(userName = username.toString())
                        }

                        override fun afterTextChanged(p0: Editable?) {
                        }
                    })
                }
            }
        }
    }

    private fun updateNutrientPreference(position: Int, nutrientPreferenceType: NutrientPreferenceType){
        val item = profileDetailsListItems[position] as ProfileDetailsListItems.DietaryPreferences
        profileDetailsListItems[position] = item.copy(
            nutrientPreference = item.nutrientPreference.copy(
                nutrientPreferenceType = nutrientPreferenceType
            )
        )
        Log.d("logger",profileDetailsListItems[position].toString())

        notifyItemChanged(position,profileDetailsListItems[position])
    }
}