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
import com.zero1labs.nutriscan.pages.homepage.HomePageEvent
import com.zero1labs.nutriscan.pages.homepage.HomePageViewModel
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.AppResources.isValidEmail
import com.zero1labs.nutriscan.viewModels.AppEvent
import com.zero1labs.nutriscan.viewModels.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInPage : Fragment(R.layout.fragment_sign_in_page) {

    private lateinit var viewModel: AppViewModel
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var btnSignInWithGoogle: Button

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
                    AuthStatus.SIGNED_IN -> {
                        ViewModelProvider(requireActivity())[HomePageViewModel::class.java].onEvent(
                            HomePageEvent.UpdateUserDetails
                        )
                        Log.d(TAG,"${state.authStatus}")
                        Snackbar.make(view,"Signing In...",Snackbar.LENGTH_SHORT).show()
                        //delaying navigation so that HomepageViewModel is initialized with loading state
                        //and the user is appUser is not null when signed in
                        delay(500)
                        if (state.appUser?.profileUpdated == true){
                            findNavController().navigate(R.id.homePage)
                        }else{
                            findNavController().navigate(R.id.welcome_page)
                        }

                    }
                    AuthStatus.SIGNED_OUT ->{
                        Log.d(TAG,"${state.authStatus}")
                    }
                    AuthStatus.ERROR -> {
                        Log.d(TAG,"${state.authStatus}")
                        state.errorMsg?.let { Snackbar.make(view, it,Snackbar.LENGTH_LONG).show() }
                    }

                    AuthStatus.NOT_STARTED -> {
                        Log.d(TAG,"${state.authStatus}")
                    }

                    AuthStatus.LOADING -> TODO()
                }
            }
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
            }else if (isValidEmail(email)){
                viewModel.onEvent(AuthEvent.SignInWithEmailAndPassword(email,password))
            }else{

            }
        }
    }
}
