package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.DessertFragmentModule
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.DessertFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [DessertFragmentModule::class])
interface DessertFragmentComponent {

    fun inject(fragment: DessertFragment)

    @Subcomponent.Builder
    interface Builder {

        fun dessertFragmentModule(fragmentModule: DessertFragmentModule): Builder
        fun build(): DessertFragmentComponent        //only need this for sub component, that's all
    }
}