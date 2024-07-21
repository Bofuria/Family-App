package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.SignInObserver
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthUI(): AuthUI {
        return AuthUI.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSignInObserver(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth
    ): SignInObserver = SignInObserver(context, firebaseAuth)

}