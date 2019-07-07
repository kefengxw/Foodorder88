package com.foodorder.order.di.module.activity

import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.model.data.GlideApp
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.activity.HomeActivity
import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule(private val mCountryActivity: HomeActivity) {

    @ActivityScope
    @Provides
    fun provideCountryActivity(): HomeActivity {
        return mCountryActivity
    }

    @ActivityScope
    @Provides
    fun provideGlideRequests(): GlideRequests {
        return GlideApp.with(mCountryActivity)
    }
}