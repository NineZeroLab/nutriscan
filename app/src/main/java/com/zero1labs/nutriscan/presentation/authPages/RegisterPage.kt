package com.zero1labs.nutriscan.presentation.authPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zero1labs.nutriscan.databinding.FragmentRegisterPageBinding
import com.mdev.core.utils.AppResources.TAG
import com.mdev.core.utils.getInput
import com.mdev.core.utils.isValidEmail
import com.mdev.core.utils.isValidPassword
import com.mdev.core.utils.logger
import com.mdev.core.utils.removeError
import com.mdev.core.utils.showSnackBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterPage: Fragment(){

    private lateinit var viewModel: AuthViewModel
    private lateinit var viewBinding: FragmentRegisterPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        viewBinding = FragmentRegisterPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleRegisterButton()
        handleUiState(view)
        handleLoginTextView()
    }


    private fun handleLoginTextView() {
        viewBinding.tvSignIn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    when (state.registerStatus) {
                        RegisterStatus.LOADING -> {
                            logger(state.registerStatus.name)
                            Snackbar.make(view, "trying to register", Snackbar.LENGTH_SHORT).show()
                        }

                        RegisterStatus.SUCCESS -> {
                            logger(state.registerStatus.name)
                            Log.d(TAG, state.registerStatus.name)
                            view.showSnackBar("Registration Success", Snackbar.LENGTH_SHORT)
                            findNavController().popBackStack()
                        }

                        RegisterStatus.FAILURE -> {
                            logger(state.registerStatus.name)
                            view.showSnackBar(state.errorMsg.toString(), Snackbar.LENGTH_SHORT)
                        }

                        RegisterStatus.NOT_STARTED -> {
                            logger(state.registerStatus.name)
                        }
                    }
                }
            }
        }
    }

    private fun handleRegisterButton() {
        viewBinding.btnRegister.setOnClickListener {
            viewBinding.apply {
                tilRegisterEmail.removeError()
                tilRegisterPassword.removeError()
                tilRegisterConfirmPassword.removeError()
            }
            val email = viewBinding.tilRegisterEmail.getInput()
            val password = viewBinding.tilRegisterPassword.getInput()
            val confirmPassword = viewBinding.tilRegisterConfirmPassword.getInput()
            val isValidEmail = email.isValidEmail()
            val isValidPassword = password.isValidPassword()
            if (!isValidEmail.first) {
                viewBinding.tilRegisterEmail.error = "Invalid Email"
            } else if (!isValidPassword.first) {
                viewBinding.tilRegisterPassword.apply {
                    error = isValidPassword.second
                    errorIconDrawable = null
                }
            } else if (password != confirmPassword) {
                viewBinding.apply {
                    tilRegisterPassword.error = "Passwords don't match"
                    tilRegisterPassword.errorIconDrawable = null
                    tilRegisterConfirmPassword.error = "Passwords don't match"
                    tilRegisterConfirmPassword.errorIconDrawable = null
                }
            } else{
                viewModel.onEvent(AuthEvent.RegisterUserWithEmailAndPassword(email, password))
            }
        }
    }
}