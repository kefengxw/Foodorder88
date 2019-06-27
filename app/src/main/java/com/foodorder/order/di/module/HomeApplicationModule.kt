package com.foodorder.order.di.module

import android.content.Context
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.model.data.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HomeApplicationModule(private val mHomeApp: HomeApplication) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context {
        return mHomeApp
    }

    @Singleton
    @Provides
    fun provideInstanceApp(): HomeApplication {
        return mHomeApp
    }

    @Singleton
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors.getInstanceEx()
    }
}