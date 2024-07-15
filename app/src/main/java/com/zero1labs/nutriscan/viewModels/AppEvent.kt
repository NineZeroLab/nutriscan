package com.zero1labs.nutriscan.viewModels

import com.google.gson.reflect.TypeToken
import com.zero1labs.nutriscan.data.models.MainDetailsForView
import com.zero1labs.nutriscan.data.models.remote.Product
import com.zero1labs.nutriscan.models.data.NutrientPreference
import com.zero1labs.nutriscan.models.data.ProfileDetailsListItems

sealed class AppEvent {
    data class GetProductDetails(val product: Product) : AppEvent()
    data class OnStartScan(val productId : String) : AppEvent()
    data object AddItemToHistory : AppEvent()
    data class SaveUserData(val userName: String, val dietaryPreferences: List<NutrientPreference>) : AppEvent()
    data class SignInWithToken(val idToken: String): AppEvent()
    data class SignInWithEmailAndPassword(val email: String, val password: String): AppEvent()
    data class RegisterUserWithEmailAndPassword(val email: String, val password: String): AppEvent()
    data object SignOut : AppEvent()
}