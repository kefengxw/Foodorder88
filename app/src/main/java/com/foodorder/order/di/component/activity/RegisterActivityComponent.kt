package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.RegisterActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.RegisterActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [RegisterActivityModule::class])
interface RegisterActivityComponent {

    fun inject(activity: RegisterActivity)

    @Subcomponent.Builder
    interface Builder {

        fun registerActivityModule(activityModule: RegisterActivityModule): Builder
        fun build(): RegisterActivityComponent        //only need this for sub component, that's all
    }
}