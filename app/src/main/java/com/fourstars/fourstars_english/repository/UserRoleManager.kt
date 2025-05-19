package com.fourstars.fourstars_english.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRoleManager {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getCurrentUserRole(): String {
        val uid = auth.currentUser?.uid ?: return "unknown"
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            doc.getString("role") ?: "user"
        } catch (e: Exception) {
            "user"
        }
    }
}