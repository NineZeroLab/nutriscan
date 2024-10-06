package com.mdev.core.utils

import android.util.Log
import com.mdev.core.utils.AppResources.TAG
import java.text.DecimalFormat

fun String.isValidEmail(): Pair<Boolean, String>{
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    return if (emailRegex.matches(this)){
        Pair(true, "Valid email")
    }else{
        Pair(false, "Invalid email")
    }
}
fun String.isValidPassword(): Pair<Boolean, String>{
    // Minimum 8 characters, at least one letter and one number
    val symbols = "~`!@#$%^&*()_-+={[}]|:;'\"\\<,>.?/"
    if (this.length < 8) return Pair(false, "should be at least 8 letters")
    val containsSymbol = this.any { char ->
        symbols.contains(char)
    }
    if (!containsSymbol) return Pair(false, "should contain at least 1 symbol")
    val containsUpperCase = this.any { char ->
        char.isUpperCase()
    }
    if (!containsUpperCase) return Pair(false, "should contain at least 1 uppercase letter")

    val containsDigit = this.any { char ->
        char.isDigit()
    }
    if (!containsDigit) return Pair(false, "should contain at least 1 digit")
    return Pair(true, "valid password")
}
fun logger(message: String){
    Log.d(TAG, message)
}

fun String.isValidUserName(): Boolean{
    val symbols = "~`!@#$%^&*()_-+={[}]|:;'\"\\<,>.?/"
    if (this.length < 6) return false
    if (this.any { symbols.contains(it) }) return false
    return true
}

fun Double.round(): Double{
    val decimalFormat = DecimalFormat("#.00")
    return decimalFormat.format(this).toDouble()
}

