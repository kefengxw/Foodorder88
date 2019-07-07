package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.AboutActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.AboutActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [AboutActivityModule::class])
interface AboutActivityComponent {

    fun inject(activity: AboutActivity)

    @Subcomponent.Builder
    interface Builder {

        fun aboutActivityModule(activityModule: AboutActivityModule): Builder
        fun build(): AboutActivityComponent        //only need this for sub component, that's all
    }
}