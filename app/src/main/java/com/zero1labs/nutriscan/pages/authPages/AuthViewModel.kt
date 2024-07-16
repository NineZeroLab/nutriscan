package com.zero1labs.nutriscan.pages.authPages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.zero1labs.nutriscan.utils.AppResources
import com.zero1labs.nutriscan.utils.AppResources.TAG
import com.zero1labs.nutriscan.utils.FirebaseCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val authStatus: AuthStatus = AuthStatus.SIGNED_OUT,
    val errorMsg: String? = null
)

enum class AuthStatus{
    SIGNED_IN,
    SIGNED_OUT,
    ERROR,
}


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel(){
    private val _uiState = MutableStateFlow<AuthState>(AuthState())
    val uiState = _uiState.asStateFlow()

    init {
        auth.currentUser?.let {
            _uiState.update { state ->
                state.copy(
                    authStatus = AuthStatus.SIGNED_IN
                )
            }
        }
    }

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.SignInWithEmailAndPassword ->{
                try {
                    auth.signInWithEmailAndPassword(
                        event.email, event.password
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            _uiState.update {
                                it.copy(
                                    authStatus = AuthStatus.SIGNED_IN
                                )
                            }
                        }
                    }
                }catch (e: Exception){
                    when(e){
                        is FirebaseAuthInvalidCredentialsException -> {
                            _uiState.update {
                                it.copy(
                                    authStatus = AuthStatus.ERROR,
                                    errorMsg = AppResources.INVALID_USERNAME_PASSWORD
                                )
                            }
                        }
                    }
                }
            }

            is AuthEvent.RegisterUserWithEmailAndPassword -> {
                viewModelScope.launch {
                    auth.createUserWithEmailAndPassword(
                        event.email, event.password
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val user = auth.currentUser
                            Log.d(TAG,"Successfully registered user with email and password")
                            user?.sendEmailVerification()?.addOnCompleteListener {
                                if (task.isSuccessful){
                                    Log.d(TAG,"Email verification sent to user.")
                                }else{
                                    Log.d(TAG,"Error: ${task.exception}")
                                }
                            }
                        }else{
                            Log.d(TAG,"Error: ${task.exception}")
                        }
                    }
                }
            }
        }
    }

    fun createFirebaseUser(firebaseUser: FirebaseUser){
        val user = hashMapOf(
            "name" to firebaseUser.email,
        )
        viewModelScope.launch {
            firestore.collection(FirebaseCollection.USERS).document(firebaseUser.uid).set(
                user
            )
        }
    }
}