package com.zero1labs.nutriscan.pages.authPages

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.utils.AppResources.isValidEmail
import com.zero1labs.nutriscan.utils.AppResources.isValidPassword

class RegisterPage: Fragment(R.layout.fragment_register_page){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tilEmail: TextInputLayout = view.findViewById(R.id.til_register_email)
        val tilPassword: TextInputLayout = view.findViewById(R.id.til_register_password)
        val tilConfirmPassword: TextInputLayout = view.findViewById(R.id.til_register_confirm_password)
        val tvSignIn: TextView = view.findViewById(R.id.tv_sign_in)
        val btnRegister: Button = view.findViewById(R.id.btn_register)
        val viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        val appCompatActivity = activity as AppCompatActivity
        val materialToolbar: MaterialToolbar = appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)
        materialToolbar.title = "Register Page"

        btnRegister.setOnClickListener {
            tilEmail.error = null
            tilPassword.error = null
            tilConfirmPassword.error = null
            val email = tilEmail.editText?.text.toString()
            val password = tilPassword.editText?.text.toString()
            val confirmPassword = tilConfirmPassword.editText?.text.toString()
            if (!isValidEmail(email)){
                tilEmail.error = "Invalid Email"
            }else if (!isValidPassword(password).first){
                tilPassword.error = "${isValidPassword(password).second}"
            }else if(password != confirmPassword){
                tilPassword.error = "Passwords don't match"
                tilConfirmPassword.error = "Passwords don't match"
            }else if (isValidEmail(email) && isValidPassword(password).first){
                //TODO: Register User and navigate to signIn Page
                viewModel.onEvent(AuthEvent.RegisterUserWithEmailAndPassword(email,password))
                findNavController().popBackStack()
            }else{

            }
        }

        tvSignIn.setOnClickListener {
            //TODO: navigate to sign in page
            findNavController().popBackStack()
        }


    }




}