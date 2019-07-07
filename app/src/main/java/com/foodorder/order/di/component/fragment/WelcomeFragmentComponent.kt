package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.WelcomeFragmentModule
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.WelcomeFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WelcomeFragmentModule::class])
interface WelcomeFragmentComponent {

    fun inject(fragment: WelcomeFragment)

    @Subcomponent.Builder
    interface Builder {

        fun welcomeFragmentModule(fragmentModule: WelcomeFragmentModule): Builder
        fun build(): WelcomeFragmentComponent        //only need this for sub component, that's all
    }
}