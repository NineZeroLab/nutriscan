package com.zero1labs.nutriscan.models.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.utils.cleanUpLanguageIndicator


data class Additive(
    @SerializedName("e_number")
    val eNumber: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("synonyms")
    val synonyms: List<String> = mutableListOf(),

    @SerializedName("additives_classes")
    val additivesClasses: List<String> = mutableListOf(),

    @SerializedName("anses_additives_of_interest")
    val ansesAdditivesOfInterest: String? = null,

    @SerializedName("efsa_evaluation")
    val efsaEvaluation: EFSAEvaluation? = null,

    @SerializedName("vegan")
    val vegan: String? = null,

    @SerializedName("vegetarian")
    val vegetarian: String? = null,

    @SerializedName("wikidata")
    val wikiData: String? = null,

    @SerializedName("wikipedia")
    val wikipedia: String? = null,

    @SerializedName("comment")
    val comment: String? = null,

)

data class EFSAEvaluation(
    @SerializedName("title")
    val title: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("url")
    val url: String,
)


fun getAdditivesDataFromJson(additives: List<String>, additivesData: List<Additive>): List<Additive>{
    val cleanAdditivesList = additives.map { it.cleanUpLanguageIndicator() }
    return additivesData.filter {
        it.eNumber in cleanAdditivesList
    }
}


fun parseAdditivesFromJson(context: Context): List<Additive>{
    val inputStream = context.resources.openRawResource(R.raw.additives)
    val json = inputStream.bufferedReader().use { it.readText() }
    val gson = Gson()
    val typeToken = object : TypeToken<List<Additive>>() {}.type

   return gson.fromJson<List<Additive>>(json,typeToken)

}