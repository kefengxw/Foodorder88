package com.foodorder.order.di.component

import com.foodorder.order.app.HomeApplication
import com.foodorder.order.di.component.activity.HomeActivityComponent
import com.foodorder.order.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HomeApplicationModule::class,
        ActivityModule::class,
        ServiceModule::class,
        RemoteModule::class,
        LocalModule::class,
        RepositoryModule::class,
        FirebaseModule::class
    ]
)
interface HomeApplicationComponent { //extends AndroidInjector<HomeApplication>

    fun inject(homeApplication: HomeApplication)

    fun homeActivityComponent(): HomeActivityComponent.Builder
}