package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.SettingFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.SettingFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SettingFragmentModule::class])
interface SettingFragmentComponent {

    fun inject(fragment: SettingFragment)

    @Subcomponent.Builder
    interface Builder {

        fun settingFragmentModule(fragmentModule: SettingFragmentModule): Builder
        fun build(): SettingFragmentComponent        //only need this for sub component, that's all
    }
}