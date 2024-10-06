package com.mdev.client_firebase.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mdev.client_firebase.data.remote.dto.AppUser
import com.mdev.client_firebase.domain.repository.FirebaseRepository
import com.mdev.client_firebase.utils.FirebaseCollection
import com.mdev.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): FirebaseRepository {
    override suspend fun getUserDetailsById(): Flow<Resource<AppUser>> = flow {
        emit(Resource.Loading())
        try {
            val userSnapShot = firestore.collection(FirebaseCollection.USERS)
                .document(auth.currentUser?.uid.toString())
                .get()
                .await()
            val appUser = userSnapShot.toObject(AppUser::class.java)
            emit(Resource.Success(appUser))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun registerUserByEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result.user))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun signInUserByEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result.user))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun createUserDetails(appUser: AppUser): Flow<Resource<AppUser>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(FirebaseCollection.USERS)
                .document(appUser.uid)
                .set(
                    appUser,
                    SetOptions.merge()
                ).await()
            emit(Resource.Success(appUser))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun signOutUser(): Flow<Resource<AppUser>> = flow {
        emit(Resource.Loading())
        try {
            auth.signOut()
            emit(Resource.Success(AppUser()))
        }catch (e: Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }
}