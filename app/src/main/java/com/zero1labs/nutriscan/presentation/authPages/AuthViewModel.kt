package com.zero1labs.nutriscan.presentation.authPages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zero1labs.nutriscan.domain.model.AppUser
import com.mdev.core.utils.AppResources
import com.mdev.core.utils.AppResources.TAG
import com.mdev.core.utils.FirebaseCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val signInStatus: SignInStatus = SignInStatus.SIGNED_OUT,
    val registerStatus: RegisterStatus = RegisterStatus.NOT_STARTED,
    val errorMsg: String? = null,
    val appUser: AppUser? =null,
)

enum class SignInStatus{
    LOADING,
    SIGNED_IN,
    SIGNED_OUT,
    ERROR,
    NOT_STARTED
}
enum class RegisterStatus{
    LOADING,
    SUCCESS,
    FAILURE,
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
                    signInStatus = SignInStatus.SIGNED_IN
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
                        }else{
                            Log.d(TAG,task.exception?.message.toString())
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.ERROR,
                                    errorMsg = com.mdev.core.utils.AppResources.INVALID_USERNAME_PASSWORD
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.NOT_STARTED
                                )
                            }
                        }
                    }
                }catch (e: Exception){
                    Log.d(TAG,e.message.toString())
                    when(e){
                        is FirebaseAuthInvalidCredentialsException -> {

                        }
                        else -> {
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.ERROR,
                                    errorMsg = com.mdev.core.utils.AppResources.INVALID_USERNAME_PASSWORD
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    signInStatus = SignInStatus.NOT_STARTED
                                )
                            }
                        }
                    }
                }
            }

            is AuthEvent.RegisterUserWithEmailAndPassword -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            registerStatus = RegisterStatus.LOADING
                        )
                    }
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
                            _uiState.update {
                                it.copy(
                                    registerStatus = RegisterStatus.SUCCESS,
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    registerStatus = RegisterStatus.NOT_STARTED
                                )
                            }
                        }else{
                            _uiState.update {
                                it.copy(
                                    registerStatus = RegisterStatus.FAILURE,
                                    errorMsg = task.exception?.message.toString()
                                )
                            }
                            _uiState.update {
                                it.copy(
                                    registerStatus = RegisterStatus.NOT_STARTED
                                )
                            }
                            Log.d(TAG,"Error: ${task.exception}")
                        }
                    }
                }
            }
        }
    }

    private fun fetchUserDetails() {
        firestore.collection(com.mdev.core.utils.FirebaseCollection.USERS)
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {userSnapshot ->
                _uiState.update { state ->
                    state.copy(
                        appUser = userSnapshot.toObject(AppUser::class.java),
                        signInStatus = SignInStatus.SIGNED_IN
                    )
                }
                _uiState.update {
                    it.copy(
                        signInStatus = SignInStatus.NOT_STARTED,
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
        firestore.collection(com.mdev.core.utils.FirebaseCollection.USERS).document(firebaseUser.uid).set(
            appUser, SetOptions.merge()
        )
    }
}