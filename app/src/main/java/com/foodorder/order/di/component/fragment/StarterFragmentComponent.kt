package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.StarterFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.StarterFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [StarterFragmentModule::class])
interface StarterFragmentComponent {

    fun inject(fragment: StarterFragment)

    @Subcomponent.Builder
    interface Builder {

        fun starterFragmentModule(fragmentModule: StarterFragmentModule): Builder
        fun build(): StarterFragmentComponent        //only need this for sub component, that's all
    }
}