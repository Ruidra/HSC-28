package com.example.data.remote

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthHelper(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val credentialManager = CredentialManager.create(context)

    suspend fun signInWithGoogle(): Boolean {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("YOUR_WEB_CLIENT_ID_HERE") // Placeholder
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is GoogleIdTokenCredential) {
                val idToken = credential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuthHelper", "Sign-in failed", e)
            false
        }
    }

    fun isUserSignedIn(): Boolean = auth.currentUser != null
    
    fun getUserId(): String? = auth.currentUser?.uid
}

class FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveUserProfile(name: String, email: String) {
        val uid = auth.currentUser?.uid ?: return
        val userMap = hashMapOf(
            "name" to name,
            "email" to email
        )
        try {
            db.collection("users").document(uid).set(userMap).await()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error saving profile", e)
        }
    }

    suspend fun saveChatHistory(chatData: Map<String, Any>) {
        val uid = auth.currentUser?.uid ?: return
        try {
            db.collection("users").document(uid).collection("chats").add(chatData).await()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error saving chat", e)
        }
    }
}
