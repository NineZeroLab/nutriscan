package com.zero1labs.nutriscan.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG){
    Snackbar.make(this, message, duration).show()
}

fun View.hideKeyBoard(){
    val imm: InputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard(){
    val imm: InputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}
fun TextInputLayout.removeError(){
    this.error = null
}

fun TextInputLayout.getInput(): String{
    return  this.editText?.text.toString()
}

fun ImageView.addImage(@DrawableRes id: Int){
    Glide.with(this)
        .load(id)
        .into(this)
}

fun ImageView.addImageFromUrl(imageUrl: String ,@DrawableRes errorImage: Int){
    Glide.with(this)
        .load(imageUrl)
        .error(errorImage)
        .into(this)
}
fun View.isVisible(): Boolean{
    return this.visibility == View.VISIBLE
}