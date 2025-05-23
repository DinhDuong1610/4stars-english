package com.fourstars.fourstars_english.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class GoogleAuthRepository(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("314263344073-qp53dm9v72pennniies2qvvs57lm6e8t.apps.googleusercontent.com")
            .requestEmail()
            .build()
    )

    /**
     * Lấy Intent để mở màn hình đăng nhập Google
     */
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    /**
     * Xử lý kết quả từ Google Sign-In
     */
    suspend fun handleSignInResult(data: Intent?): Boolean {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
            val idToken = account.idToken
            if (idToken.isNullOrEmpty()) {
                Log.e("GoogleAuth", "⚠️ Google ID Token is null!")
                return false
            }

            return firebaseAuthWithGoogle(idToken)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "❌ Error processing Google Sign-In result: ${e.message}")
            return false
        }
    }

    /**
     * Đăng nhập Firebase bằng Google ID Token
     */
    suspend fun firebaseAuthWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user

            user?.let {
                saveUserToFirestore(it)
            }
            true
        } catch (e: Exception) {
            Log.e("GoogleAuth", "❌ Firebase Authentication failed: ${e.message}")
            false
        }
    }

    /**
     * Lưu thông tin người dùng vào Firestore
     */
    private fun saveUserToFirestore(user: FirebaseUser) {
        val userRef = firestore.collection("users").document(user.uid)

        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to (user.displayName ?: ""),
            "email" to (user.email ?: ""),
            "photoUrl" to (user.photoUrl?.toString() ?: ""),
            "provider" to "google",
            "role" to "admin"
        )

        userRef.set(userData, SetOptions.merge()) // ✅ Chỉ thêm các trường mới, không ghi đè nếu đã có
            .addOnSuccessListener {
                Log.d("Firestore", "User data saved/merged successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving user data", e)
            }
    }

    /**
     * Đăng xuất người dùng khỏi Google & Firebase
     */
    suspend fun signOut() {
        try {
            googleSignInClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            Log.e("GoogleAuth", "❌ Sign out failed: ${e.message}")
        }
    }
}
