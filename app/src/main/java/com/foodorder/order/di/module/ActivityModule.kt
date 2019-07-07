package com.foodorder.order.di.module

import com.foodorder.order.di.component.activity.*

import dagger.Module

@Module(
    subcomponents = [
        HomeActivityComponent::class,
        SubmitActivityComponent::class,
        FoodStatusDisplayActivityComponent::class,
        ChefWaiterActivityComponent::class,
        AboutActivityComponent::class,
        InitialActivityComponent::class,
        ManagerActivityComponent::class,
        SettingActivityComponent::class,
        EditProfileActivityComponent::class,
        UploadOverviewActivityComponent::class,
        UploadEditActivityComponent::class,
        RegisterActivityComponent::class
    ]
)//if more activity, please add here
class ActivityModule//Only designed for sub-component