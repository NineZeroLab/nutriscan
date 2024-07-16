package com.zero1labs.nutriscan.data.models

import android.media.Image
import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class SearchHistoryListItem(
    val mainDetailsForView: MainDetailsForView =  MainDetailsForView(),
    val timeStamp : Timestamp = Timestamp.now()
)
