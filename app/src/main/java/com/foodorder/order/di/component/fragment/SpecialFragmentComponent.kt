package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.SpecialFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.SpecialFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SpecialFragmentModule::class])
interface SpecialFragmentComponent {

    fun inject(fragment: SpecialFragment)

    @Subcomponent.Builder
    interface Builder {

        fun specialFragmentModule(fragmentModule: SpecialFragmentModule): Builder
        fun build(): SpecialFragmentComponent        //only need this for sub component, that's all
    }
}