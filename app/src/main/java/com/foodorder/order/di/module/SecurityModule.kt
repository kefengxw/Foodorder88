package com.foodorder.order.di.module

import android.content.SharedPreferences
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SecurityModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(it: HomeApplication): SharedPreferences {
        return SecuritySharedPreferencesFactory.getInstanceSSharedPre(it)
    }
}