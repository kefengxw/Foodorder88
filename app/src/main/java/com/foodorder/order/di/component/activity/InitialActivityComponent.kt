package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.InitialActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.InitialActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [InitialActivityModule::class])
interface InitialActivityComponent {

    fun inject(activity: InitialActivity)

    @Subcomponent.Builder
    interface Builder {

        fun initialActivityModule(activityModule: InitialActivityModule): Builder
        fun build(): InitialActivityComponent        //only need this for sub component, that's all
    }
}