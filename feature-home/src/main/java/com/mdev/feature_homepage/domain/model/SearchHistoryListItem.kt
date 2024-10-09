package com.mdev.feature_homepage.domain.model

import com.google.firebase.Timestamp

data class SearchHistoryListItem(
    val mainDetailsForView: MainDetailsForView =  MainDetailsForView(),
    val timeStamp : Timestamp = Timestamp.now()
)
