package com.zero1labs.nutriscan.data.models

import android.media.Image
import java.time.LocalDateTime

data class SearchHistoryListItem(
    val mainDetailsForView: MainDetailsForView,
    val timeStamp : LocalDateTime
)
