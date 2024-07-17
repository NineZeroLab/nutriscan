package com.zero1labs.nutriscan.pages.authPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel
import kotlinx.coroutines.launch

class SignInPage : Fragment(R.layout.fragment_sign_in_page) {

    private lateinit var viewModel: AppViewModel
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var btnSignInWithGoogle: Button
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignInWithGoogle = view.findViewById(R.id.btn_sign_in_google)
        // Initialize oneTapClient and signInRequest
        oneTapClient = Identity.getSignInClient(requireActivity())

        val viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        val tilEmail: TextInputLayout = view.findViewById(R.id.til_email)
        val tilPassword: TextInputLayout = view.findViewById(R.id.til_password)
        val btnSignIn: Button = view.findViewById(R.id.btn_sign_in)
        val tvRegister: TextView = view.findViewById(R.id.tv_register)

        val appCompatActivity = activity as AppCompatActivity
        val materialToolbar: MaterialToolbar = appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)
        materialToolbar.title = "SignIn Page"


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect{state ->
                when(state.authStatus){
                    AuthStatus.SIGNED_IN -> findNavController().navigate(R.id.action_sign_in_page_to_homepage)
                    AuthStatus.SIGNED_OUT ->{}
                    AuthStatus.ERROR -> {
                        state.errorMsg?.let { Snackbar.make(view, it,Snackbar.LENGTH_LONG).show() }
                    }
                }
            }
        }

        val serverId = "2576807066-9afs0212vqme609adkdbapfihdgu04iq.apps.googleusercontent.com"

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                /* googleIdTokenRequestOptions = */ BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(serverId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        btnSignInWithGoogle.setOnClickListener{
            startOneTapSignIn()
        }

        tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_sign_in_page_to_register_page)
        }

        btnSignIn.setOnClickListener {
            tilEmail.error = null
            tilPassword.error = null
            val email = tilEmail.editText?.text.toString()
            val password = tilPassword.editText?.text.toString()

            if (!isValidEmail(email)) {
                tilEmail.error = "Invalid Email"
            } else if (!isValidPassword(password)){
                tilPassword.error = "Invalid password"
            }else if (isValidEmail(email) && isValidPassword(password)){
                //TODO: Send Email and password to ViewModel
                viewModel.onEvent(AuthEvent.SignInWithEmailAndPassword(email,password))
            }else{

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_ONE_TAP) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate with Firebase.
                        Log.d(TAG, "Got ID token.")
                        viewModel.onEvent(AppEvent.SignInWithToken(idToken))

                    }
                    else -> {
                        // Shouldn't happen.
                        Log.d(TAG, "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                // Handle error
                Log.e(TAG, "Sign-in failed", e)
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        // Minimum 8 characters, at least one letter and one number
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$".toRegex()
        return passwordRegex.matches(password)
    }


    private fun startOneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                Log.d(TAG, e.localizedMessage)
            }
    }


}