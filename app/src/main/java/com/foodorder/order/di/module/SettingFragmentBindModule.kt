package com.foodorder.order.di.module

import com.foodorder.order.di.component.fragment.SettingFragmentComponent
import dagger.Module

//@Module
@Module(
    subcomponents = [
        SettingFragmentComponent::class
    ]
)//if more activity, please add here
class SettingFragmentBindModule//Only designed for sub-component