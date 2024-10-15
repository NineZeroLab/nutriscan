package com.mdev.openfoodfacts_client.data.remote.dto


import com.google.gson.annotations.SerializedName

data class AdditiveDto(
    @SerializedName("additives_classes")
    val additivesClasses: List<String?>? = null,
    @SerializedName("anses_additives_of_interest")
    val ansesAdditivesOfInterest: String? = null,
    @SerializedName("comment")
    val comment: String? = null,
    @SerializedName("e_number")
    val eNumber: String? = null,
    @SerializedName("efsa_evaluation")
    val efsaEvaluation: EfsaEvaluation? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("synonyms")
    val synonyms: List<String?>? = null,
    @SerializedName("vegan")
    val vegan: String? = null,
    @SerializedName("vegetarian")
    val vegetarian: String? = null,
    @SerializedName("wikidata")
    val wikidata: String? = null,
    @SerializedName("wikipedia")
    val wikipedia: String? = null
)