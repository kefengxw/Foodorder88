package com.foodorder.order.di.component.activity

import com.foodorder.order.di.module.SettingFragmentBindModule
import com.foodorder.order.di.module.activity.SettingActivityModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.view.activity.SettingActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        SettingActivityModule::class,
        SettingFragmentBindModule::class
    ]
)
interface SettingActivityComponent {

    fun inject(activity: SettingActivity)

    @Subcomponent.Builder
    interface Builder {

        fun settingActivityModule(activityModule: SettingActivityModule): Builder
        fun build(): SettingActivityComponent        //only need this for sub component, that's all
    }
}