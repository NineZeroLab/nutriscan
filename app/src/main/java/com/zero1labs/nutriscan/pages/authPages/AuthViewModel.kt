package com.zero1labs.nutriscan.pages.authPages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zero1labs.nutriscan.models.data.AppUser
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
    val errorMsg: String? = null,
    val appUser: AppUser? =null,
)

enum class AuthStatus{
    LOADING,
    SIGNED_IN,
    SIGNED_OUT,
    ERROR,
    NOT_STARTED
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
                            fetchUserDetails()


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
                            _uiState.update {
                                it.copy(
                                    authStatus = AuthStatus.NOT_STARTED
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
                            if (user != null) {
                                createFirebaseUser(user)
                            }
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

    private fun fetchUserDetails() {
        firestore.collection(FirebaseCollection.USERS)
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {userSnapshot ->
                _uiState.update { state ->
                    state.copy(
                        appUser = userSnapshot.toObject(AppUser::class.java),
                        authStatus = AuthStatus.SIGNED_IN
                    )
                }
                _uiState.update {
                    it.copy(
                        authStatus = AuthStatus.NOT_STARTED,
                        appUser = null
                    )
                }
            }
    }

    private fun createFirebaseUser(firebaseUser: FirebaseUser){
        val appUser = AppUser(
            name = firebaseUser.email.toString(),
            uid = firebaseUser.uid
        )
        firestore.collection(FirebaseCollection.USERS).document(firebaseUser.uid).set(
            appUser, SetOptions.merge()
        )
    }
}