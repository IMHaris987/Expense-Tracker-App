package com.haris.expensetracker.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class FirebaseHelper {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(
        name: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }

                    auth.currentUser?.updateProfile(profileUpdates)

                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun login(
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun passwordForgot(
        email: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }
}
