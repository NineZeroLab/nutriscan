package com.mdev.feature_homepage.domain.model

import com.google.firebase.Timestamp
import com.mdev.client_firebase.data.remote.dto.MainDetailsForView

data class SearchHistoryListItem(
    val mainDetailsForView: MainDetailsForView =  MainDetailsForView(),
    val timeStamp : Timestamp = Timestamp.now()
)
