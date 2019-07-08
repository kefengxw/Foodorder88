package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.model.data.AppExecutors
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    //Should not include any context, activity, fragment and so on
    protected lateinit var mEx: AppExecutors
    protected lateinit var mInstanceApp: HomeApplication
    //1. private lateinit var mDisposable: CompositeDisposable

    init {
        initBaseViewModel(app)
    }

    private fun initBaseViewModel(app: Application) {
        //2. mDisposable = CompositeDisposable()
        mInstanceApp = app as HomeApplication//this.getApplication<HomeApplication>()
        mEx = mInstanceApp.mInstanceEx

        initInjector(getApplicationComponent())
        initViewModel(app)
    }

    abstract fun initViewModel(app: Application)
    abstract fun initInjector(component: HomeApplicationComponent)

    fun getApplicationComponent(): HomeApplicationComponent {
        return mInstanceApp.getApplicationComponent()
    }

    protected fun addDisposable(it: Disposable) {
        //3. mDisposable.add(it)
    }

    override fun onCleared() {
        //4. clearDisposable()
        super.onCleared()
    }

    private fun clearDisposable() {
        //5. mDisposable.clear()
    }
}