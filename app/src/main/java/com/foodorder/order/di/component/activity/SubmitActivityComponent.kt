package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.SubmitActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.SubmitActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SubmitActivityModule::class])
interface SubmitActivityComponent {

    fun inject(activity: SubmitActivity)

    @Subcomponent.Builder
    interface Builder {

        fun submitActivityModule(activityModule: SubmitActivityModule): Builder
        fun build(): SubmitActivityComponent        //only need this for sub component, that's all
    }
}