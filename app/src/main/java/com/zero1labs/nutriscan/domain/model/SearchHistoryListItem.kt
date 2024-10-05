package com.zero1labs.nutriscan.domain.model

import com.google.firebase.Timestamp

data class SearchHistoryListItem(
    val mainDetailsForView: MainDetailsForView =  MainDetailsForView(),
    val timeStamp : Timestamp = Timestamp.now()
)
