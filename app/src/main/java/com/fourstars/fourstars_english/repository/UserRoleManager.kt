package com.fourstars.fourstars_english.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRoleViewModel : ViewModel() {
    var isAdmin by mutableStateOf(false)
        private set

    fun checkUserRole(uid: String) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val role = doc.getString("role")
                isAdmin = role == "admin"
            }
            .addOnFailureListener {
                isAdmin = false
            }
    }
}
