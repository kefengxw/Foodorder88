package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.ManagerTestFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.ManagerTestFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ManagerTestFragmentModule::class])
interface ManagerTestFragmentComponent {

    fun inject(fragment: ManagerTestFragment)

    @Subcomponent.Builder
    interface Builder {

        fun managerTestFragmentModule(fragmentModule: ManagerTestFragmentModule): Builder
        fun build(): ManagerTestFragmentComponent        //only need this for sub component, that's all
    }
}