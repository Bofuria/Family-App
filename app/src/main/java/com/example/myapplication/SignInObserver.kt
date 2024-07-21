package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.myapplication.auth.FirebaseUIActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInObserver @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) : DefaultLifecycleObserver {

    private var _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn = _isUserSignedIn.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        _isUserSignedIn.value = user != null
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    fun signOut() {
        AuthUI.getInstance()
            .signOut(context)
//            .addOnCompleteListener {
//                val intent = Intent(context, FirebaseUIActivity::class.java)
//                context.startActivity(intent)
//                (context as? Activity)?.finish()
//            }
    }
}