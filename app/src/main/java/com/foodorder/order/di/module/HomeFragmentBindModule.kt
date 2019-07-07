package com.foodorder.order.di.module

import com.foodorder.order.di.component.fragment.*
import dagger.Module

//@Module
@Module(
    subcomponents = [
        MainCourseFragmentComponent::class,
        StarterFragmentComponent::class,
        WineDrinkFragmentComponent::class,
        DessertFragmentComponent::class,
        SpecialFragmentComponent::class,
        WelcomeFragmentComponent::class,
        ConceptFragmentComponent::class,
        ChefTestFragmentComponent::class,
        ManagerTestFragmentComponent::class
    ]
)//if more activity, please add here
class HomeFragmentBindModule//Only designed for sub-component