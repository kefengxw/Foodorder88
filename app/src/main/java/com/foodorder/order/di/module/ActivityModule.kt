package com.foodorder.order.di.module

import com.foodorder.order.di.component.HomeActivityComponent

import dagger.Module

@Module(subcomponents = [HomeActivityComponent::class])//if more activity, please add here
class ActivityModule//Only designed for sub-component