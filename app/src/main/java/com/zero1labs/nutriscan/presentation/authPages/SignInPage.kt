package com.zero1labs.nutriscan.presentation.authPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.databinding.FragmentSignInPageBinding
import com.zero1labs.nutriscan.presentation.homepage.HomePageEvent
import com.zero1labs.nutriscan.presentation.homepage.HomePageViewModel
import com.mdev.core.utils.AppResources.TAG
import com.mdev.core.utils.getInput
import com.mdev.core.utils.isValidEmail
import com.mdev.core.utils.logger
import com.mdev.core.utils.removeError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInPage : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var viewBinding: FragmentSignInPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSignInPageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUiState(view)
        handleRegisterTextView()
        handleSignInButton()
    }

    private fun handleSignInButton() {
        viewBinding.btnSignIn.setOnClickListener {
            viewBinding.apply {
                tilEmail.removeError()
                tilPassword.removeError()
            }
            val email = viewBinding.tilEmail.getInput()
            val password = viewBinding.tilPassword.getInput()

            if (!email.isValidEmail().first) {
                viewBinding.tilEmail.error = "Invalid Email"
            } else {
                viewModel.onEvent(AuthEvent.SignInWithEmailAndPassword(email, password))
            }
        }
    }

    private fun handleRegisterTextView() {
        viewBinding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_sign_in_page_to_register_page)
        }
    }

    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state.signInStatus) {
                    SignInStatus.SIGNED_IN -> {
                        ViewModelProvider(requireActivity())[HomePageViewModel::class.java].onEvent(
                            HomePageEvent.UpdateUserDetails
                        )
                        Log.d(TAG, "${state.signInStatus}")
                        Snackbar.make(view, "Signing In...", Snackbar.LENGTH_SHORT).show()
                        //delaying navigation so that HomepageViewModel is initialized with loading state
                        //and the user is appUser is not null when signed in
                        delay(500)
                        if (state.appUser?.profileUpdated == true) {
                            logger("navigating to homepage")
                            findNavController().navigate(R.id.homePage)
                        } else {
                            logger("navigating to profile page")
                            findNavController().navigate(R.id.welcome_page)
                        }
                    }

                    SignInStatus.SIGNED_OUT -> {
                        Log.d(TAG, "${state.signInStatus}")
                    }

                    SignInStatus.ERROR -> {
                        Log.d(TAG, "${state.signInStatus}")
                        viewBinding.tilPassword.error = state.errorMsg
                    }

                    SignInStatus.NOT_STARTED -> {
                        Log.d(TAG, "${state.signInStatus}")
                    }

                    SignInStatus.LOADING -> {}
                }
            }
        }
    }
}
