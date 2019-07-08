package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.model.data.InternalDataConfiguration.shouldTakeToken
import com.foodorder.order.model.data.NetworkState
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.data.ResourceListing
import com.foodorder.order.model.paging.RemoteDataPageRepository
import com.foodorder.order.model.repository.DataRepository
import com.foodorder.order.model.repository.DisplayItem

class RecyListDataViewModel(app: Application) : BaseViewModel(app) {

    private lateinit var mReposData: DataRepository
    private lateinit var mUserAuth: LiveData<Resource<String>>
    private lateinit var mPageRepos: RemoteDataPageRepository
    private val mFilter = MutableLiveData<String>()
    private lateinit var mRemoteData: LiveData<ResourceListing<DisplayItem>>
    private lateinit var mResultData: LiveData<PagedList<DisplayItem>>
    private lateinit var mResultState: LiveData<NetworkState>
    private var mPrevLength = 0

/*    init {//can be replace by ViewModel not android view model
        initViewModel(app)
    }

    private fun initViewModel(app: Application) {

        val instanceApp = app as HomeApplication//this.getApplication<HomeApplication>()

        mEx = instanceApp.mInstanceEx
        mReposData = instanceApp.mInstanceRepos
        mPageRepos = instanceApp.mInstancePageRepos
        doUserAuth()
        initFilterData()
    }*/

    override fun initInjector(component: HomeApplicationComponent){

    }

    override fun initViewModel(app: Application) {
        mReposData = mInstanceApp.mInstanceRepos
        mPageRepos = mInstanceApp.mInstancePageRepos
        doUserAuth()
        initFilterData()
    }

    private fun doUserAuth() {
        if (!shouldTakeToken()) return
        //do auth for user
        mUserAuth = LiveDataReactiveStreams.fromPublisher(mReposData.getRemoteToken())
    }

    private fun initFilterData() {
        mRemoteData = map(mFilter) { getDataByName(it) }
        mResultData = switchMap(mRemoteData) { it.mPagedList }
        mResultState = switchMap(mRemoteData) { it.mNetworkState }
    }

    fun getAuthData(): LiveData<Resource<String>> {
        return mUserAuth
    }

    private fun getDataByName(input: String): ResourceListing<DisplayItem> {
        return mPageRepos.getRemoteDataPageResult(input)
    }

    fun getFilterData(): LiveData<PagedList<DisplayItem>> {
        return mResultData
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return mResultState
    }

    fun setFilter(input: String) {
        //all the logical is done by ViewModel
        val it = input.trim().toLowerCase()
        val tmp = it.length

        if (tmp > 0 || mPrevLength > 0) {//only handle valid input, all logical is done by ViewModel
            mFilter.value = it//Improvement 1: To avoid subscribe and unsubscribe each time
        }
        mPrevLength = tmp//to fix the input from 1 -> 0 case
        //TBD: check same or not
    }

    override fun onCleared() {
        super.onCleared()
    }
}