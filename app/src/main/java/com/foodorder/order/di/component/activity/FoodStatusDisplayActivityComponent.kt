package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.FoodStatusDisplayActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.FoodStatusDisplayActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [FoodStatusDisplayActivityModule::class])
interface FoodStatusDisplayActivityComponent {

    fun inject(activity: FoodStatusDisplayActivity)

    @Subcomponent.Builder
    interface Builder {

        fun foodStatusDisplayActivityModule(activityModule: FoodStatusDisplayActivityModule): Builder
        fun build(): FoodStatusDisplayActivityComponent        //only need this for sub component, that's all
    }
}