package com.zero1labs.nutriscan.utils

import java.time.Duration

class TimeCalculator {
    companion object{
        fun getTime( duration: Duration) : String{
            if (duration.toDays() < 1){
                return if (duration.toHours() < 1 ){
                    "Just Now"
                } else{
                    "${duration.toHours().toString()} hours ago"
                }
            }
            return "${duration.toDays()} days ago"
        }
    }
}