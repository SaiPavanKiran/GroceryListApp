package com.rspk.grocerylistapp.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

val firebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
    .setLocalCacheSettings(
        memoryCacheSettings {
            FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }
    )
    .build()

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    init {
        firestore().firestoreSettings = firebaseFirestoreSettings
    }

    @Provides fun auth(): FirebaseAuth = Firebase.auth

    @Provides fun firestore(): FirebaseFirestore = Firebase.firestore

    @Provides fun storage(): FirebaseStorage = Firebase.storage
}
