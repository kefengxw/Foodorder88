package com.foodorder.order.di.component

import com.foodorder.order.app.HomeApplication
import com.foodorder.order.di.component.activity.HomeActivityComponent
import com.foodorder.order.di.module.*
import com.foodorder.order.di.module.viewmodel.ViewModelModule
import com.foodorder.order.viewmodel.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        HomeApplicationModule::class,
        ActivityBindModule::class,
        ServiceModule::class,
        RemoteModule::class,
        LocalModule::class,
        RepositoryModule::class,
        FirebaseModule::class,
        SecurityModule::class
    ]
)
interface HomeApplicationComponent { //extends AndroidInjector<HomeApplication>

    fun inject(injector: HomeApplication)
    fun inject(injector: HomeViewModel)

    fun homeActivityComponent(): HomeActivityComponent.Builder
}