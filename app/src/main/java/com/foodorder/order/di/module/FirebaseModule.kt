package com.foodorder.order.di.module

import com.foodorder.order.model.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthRepository(fbAuth: FirebaseAuth): FirebaseAuthRepository {
        return FirebaseAuthRepositoryFactory.getInstanceFbAuthRepos(fbAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseCloudDbRepository(
        fbCloudDatabase: FirebaseFirestore,
        fbCloudStorage: FirebaseStorage
    ): FirebaseCloudDbRepository {
        return FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos(fbCloudDatabase, fbCloudStorage)
    }

    @Singleton
    @Provides
    fun provideFirebaseCloudQueryRepository(
        fbCloudDatabase: FirebaseFirestore,
        fbCloudStorage: FirebaseStorage
    ): FirebaseCloudQueryRepository {
        return FirebaseCloudQueryRepositoryFactory.getInstanceFbCloudQueryRepos(fbCloudDatabase, fbCloudStorage)
    }
}