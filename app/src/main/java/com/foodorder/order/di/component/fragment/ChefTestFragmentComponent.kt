package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.ChefTestFragmentModule
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.ChefTestFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ChefTestFragmentModule::class])
interface ChefTestFragmentComponent {

    fun inject(fragment: ChefTestFragment)

    @Subcomponent.Builder
    interface Builder {

        fun chefTestFragmentModule(fragmentModule: ChefTestFragmentModule): Builder
        fun build(): ChefTestFragmentComponent        //only need this for sub component, that's all
    }
}