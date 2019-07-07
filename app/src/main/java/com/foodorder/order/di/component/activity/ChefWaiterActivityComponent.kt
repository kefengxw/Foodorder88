package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.ChefWaiterActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.ChefWaiterActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ChefWaiterActivityModule::class])
interface ChefWaiterActivityComponent {

    fun inject(activity: ChefWaiterActivity)

    @Subcomponent.Builder
    interface Builder {

        fun chefWaiterActivityModule(activityModule: ChefWaiterActivityModule): Builder
        fun build(): ChefWaiterActivityComponent        //only need this for sub component, that's all
    }
}