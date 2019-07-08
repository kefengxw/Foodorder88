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
}