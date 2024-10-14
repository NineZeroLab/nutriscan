package com.mdev.openfoodfacts_client.domain.model

enum class PointsLevel (val description: String){
    TOO_LOW ("very low"),
    LOW ("low"),
    MODERATE ("moderate"),
    HIGH ("high"),
    TOO_HIGH ("very high"),
    UNKNOWN ("unknown")
}