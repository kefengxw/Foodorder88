package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.MainCourseFragmentModule
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.MainCourseFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [MainCourseFragmentModule::class])
interface MainCourseFragmentComponent {

    fun inject(fragment: MainCourseFragment)

    @Subcomponent.Builder
    interface Builder {

        fun mainCourseFragmentModule(fragmentModule: MainCourseFragmentModule): Builder
        fun build(): MainCourseFragmentComponent        //only need this for sub component, that's all
    }
}