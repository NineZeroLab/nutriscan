package com.mdev.openfoodfacts_client.data.remote.dto


import com.google.gson.annotations.SerializedName

data class EfsaEvaluation(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("url")
    val url: String? = null
)