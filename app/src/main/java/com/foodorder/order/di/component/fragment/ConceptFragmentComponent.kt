package com.foodorder.order.di.component.fragment

import com.foodorder.order.di.module.fragment.ConceptFragmentModule
import com.foodorder.order.di.scope.ActivityScope
import com.foodorder.order.di.scope.FragmentScope
import com.foodorder.order.view.fragment.ConceptFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [ConceptFragmentModule::class])
interface ConceptFragmentComponent {

    fun inject(fragment: ConceptFragment)

    @Subcomponent.Builder
    interface Builder {

        fun conceptFragmentModule(fragmentModule: ConceptFragmentModule): Builder
        fun build(): ConceptFragmentComponent        //only need this for sub component, that's all
    }
}