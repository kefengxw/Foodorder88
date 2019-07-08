package com.foodorder.order.viewmodel

import android.app.Application
import com.foodorder.order.di.component.HomeApplicationComponent

class SettingViewModel(app: Application) : BaseViewModelWithLogin(app) {

    override fun initInjector(component: HomeApplicationComponent){

    }

    override fun initSubViewModel(app: Application) {

    }
}