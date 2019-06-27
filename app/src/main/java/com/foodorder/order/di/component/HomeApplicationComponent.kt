package com.foodorder.order.di.component

import com.foodorder.order.app.HomeApplication
import com.foodorder.order.di.module.ActivityModule
import com.foodorder.order.di.module.HomeApplicationModule
import com.foodorder.order.di.module.RemoteModule
import com.foodorder.order.di.module.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HomeApplicationModule::class,
        RemoteModule::class,
        RepositoryModule::class,
        ActivityModule::class]
)
interface HomeApplicationComponent { //extends AndroidInjector<HomeApplication>

    fun inject(homeApplication: HomeApplication)

    fun homeActivityComponent(): HomeActivityComponent.Builder
}