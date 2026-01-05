package com.haris.expensetracker.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await


sealed class Result <out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}

class AuthRepository(private val auth: FirebaseAuth) {

    suspend fun login(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user ?: throw Exception("User Not Found")
                Result.Success(user)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }

    suspend fun register(name: String, email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user ?: throw Exception("User Creation Failed")

                val profileUpdated = userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdated).await()
                Result.Success(user)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }

    suspend fun passwordForgot(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                auth.sendPasswordResetEmail(email).await()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        }
}