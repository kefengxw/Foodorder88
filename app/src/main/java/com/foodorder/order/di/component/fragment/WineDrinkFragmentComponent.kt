package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.WineDrinkFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.WineDrinkFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [WineDrinkFragmentModule::class])
interface WineDrinkFragmentComponent {

    fun inject(fragment: WineDrinkFragment)

    @Subcomponent.Builder
    interface Builder {

        fun wineDrinkFragmentModule(fragmentModule: WineDrinkFragmentModule): Builder
        fun build(): WineDrinkFragmentComponent        //only need this for sub component, that's all
    }
}