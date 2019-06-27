package com.foodorder.order.di.module

import android.content.SharedPreferences
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.firebase.FirebaseAuthRepository
import com.foodorder.order.model.firebase.FirebaseAuthRepositoryFactory
import com.foodorder.order.model.remote.RemoteDataRepository
import com.foodorder.order.model.repository.DataRepository
import com.foodorder.order.model.repository.DataRepositoryFactory
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideDataRepository(appEx: AppExecutors, remoteRepos: RemoteDataRepository): DataRepository {
        return DataRepositoryFactory.getInstanceRepos(appEx, remoteRepos)
    }

    //需要进行移动到专门的文件中firebaseRepositoryModule
    @Singleton
    @Provides
    fun provideFirebaseRepository(): FirebaseAuthRepository {
        return FirebaseAuthRepositoryFactory.getInstanceFbAuthRepos()
    }

    //需要进行移动到专门的文件中SecuritySharedPreferencesFactory
    @Singleton
    @Provides
    fun provideSharedPreferences(it: HomeApplication): SharedPreferences {
        return SecuritySharedPreferencesFactory.getInstanceSSharedPre(it)
    }

//    class FirebaseModule {
//
//        @Singleton
//        @Provides
//        fun provideFirebaseAuth(): FirebaseAuth {
//            return FirebaseAuth.getInstance()
//        }
//    }
}