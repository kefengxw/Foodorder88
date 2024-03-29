package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.model.data.InternalDataConfiguration
import com.foodorder.order.model.data.NetworkState
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.data.ResourceListing
import com.foodorder.order.model.firebase.FirebaseCloudDbRepository
import com.foodorder.order.model.paging.RemoteDataPageRepository
import com.foodorder.order.model.repository.DataRepository
import com.foodorder.order.model.repository.DisplayItem
import com.google.firebase.firestore.Query
import javax.inject.Inject

//@Inject
//constructor(private val repoRepository: RepoRepository)

class HomeViewModel @Inject constructor(app: Application) : BaseViewModelWithLogin(app) {
    //    class HomeViewModel(app: Application) : BaseViewModelWithLogin(app) {
    lateinit var mReposData: DataRepository
    private lateinit var mUserAuth: LiveData<Resource<String>>
    lateinit var mPageRepos: RemoteDataPageRepository
    private val mFilter = MutableLiveData<String>()
    private lateinit var mRemoteData: LiveData<ResourceListing<DisplayItem>>
    private lateinit var mResultData: LiveData<PagedList<DisplayItem>>
    private lateinit var mResultState: LiveData<NetworkState>
    private var mPrevLength = 0
    lateinit var mFbCloudDbRepos: FirebaseCloudDbRepository

    override fun initInjector(component: HomeApplicationComponent){
        component.inject(this)
    }

    override fun initSubViewModel(app: Application) {

        mReposData = mInstanceApp.mInstanceRepos
        mPageRepos = mInstanceApp.mInstancePageRepos
        //mFbCloudDbRepos = mInstanceApp.mInstanceFbCloudDbRepos
        doUserAuth()
        initFilterData()
    }

    @Inject
    fun setInstanceFbCloudDbRepos(instance: FirebaseCloudDbRepository) {
        mFbCloudDbRepos = instance
    }

    private fun doUserAuth() {
        if (!InternalDataConfiguration.shouldTakeToken()) return
        //do auth for user
        mUserAuth = LiveDataReactiveStreams.fromPublisher(mReposData.getRemoteToken())
    }

    private fun initFilterData() {
        mRemoteData = Transformations.map(mFilter) { getDataByName(it) }
        mResultData = Transformations.switchMap(mRemoteData) { it.mPagedList }
        mResultState = Transformations.switchMap(mRemoteData) { it.mNetworkState }
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

    private fun test() {}

    fun getQuery(): Query {
        //后续优化，需要考虑dagger
//        val mFbCloudDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val mCoName: String = InternalStatusConfiguration.getLoginUserId()
//        val mDbCoRef = mFbCloudDatabase.collection(mCoName)
//        val mDocFood = mDbCoRef.document("food").collection("food")
//
//        return mDocFood.orderBy("uniqueId", Query.Direction.ASCENDING)

        return mFbCloudDbRepos.getFoodQueryOrderByKey()
    }
}