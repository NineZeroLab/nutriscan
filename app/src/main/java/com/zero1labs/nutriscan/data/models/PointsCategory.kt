package com.zero1labs.nutriscan.data.models

enum class PointsCategory (val description: String){
    TOO_LOW ("too low"),
    LOW ("low"),
    MODERATE ("moderate"),
    HIGH ("high"),
    TOO_HIGH ("too high"),
    UNKNOWN ("unknown")
}