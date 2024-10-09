package com.mdev.feature_login.presentation.loginPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mdev.core.utils.getInput
import com.mdev.core.utils.isValidEmail
import com.mdev.core.utils.removeError
import com.mdev.core.utils.showSnackBar
import com.mdev.feature_login.databinding.FragmentLoginPageBinding
import com.mdev.feature_login.navigation.LoginNavigator
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginPage : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var viewBinding: FragmentLoginPageBinding
    @Inject
    private lateinit var navigator : LoginNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginPageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
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
            val email = viewBinding.tilEmail.getInput()
            val password = viewBinding.tilPassword.getInput()
            viewModel.onEvent(LoginEvent.LoginWithEmailAndPassword(email, password))
        }
//        viewBinding.btnSignIn.setOnClickListener {
//            viewBinding.apply {
//                tilEmail.removeError()
//                tilPassword.removeError()
//            }
//            val email = viewBinding.tilEmail.getInput()
//            val password = viewBinding.tilPassword.getInput()
//
//            if (!email.isValidEmail().first) {
//                viewBinding.tilEmail.error = "Invalid Email"
//            } else {
//                viewModel.onEvent(LoginEvent.LoginWithEmailAndPassword(email,password))
//            }
//        }
    }

    private fun handleRegisterTextView() {
        viewBinding.tvRegister.setOnClickListener {
            navigator.navigateToRegisterPage()
//            findNavController().navigate(R.id.action_sign_in_page_to_register_page)
        }
    }

    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state.loginStatus){
                    LoginStatus.LOADING -> {}
                    LoginStatus.SUCCESS -> {
                        navigator.navigateToHomePage()
                    }
                    LoginStatus.FAILURE -> {
                        view.showSnackBar(state.errorMessage.toString())
                    }
                    LoginStatus.IDLE -> {}
                }
            }
        }
    }
}
