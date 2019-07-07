package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.activity.ManagerActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.ManagerActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ManagerActivityModule::class])
interface ManagerActivityComponent {

    fun inject(activity: ManagerActivity)

    @Subcomponent.Builder
    interface Builder {

        fun managerActivityModule(activityModule: ManagerActivityModule): Builder
        fun build(): ManagerActivityComponent        //only need this for sub component, that's all
    }
}