package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.foodorder.order.model.data.ReturnCode
import com.foodorder.order.model.data.ReturnCode.*
import com.foodorder.order.model.firebase.*
import com.foodorder.order.model.firebase.FirebaseResult.Companion.errorFbData
import com.foodorder.order.model.firebase.FirebaseUserResult.Companion.errorFbUserData
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

class EditProfileViewModel(app: Application) : BaseViewModel(app) {

    private lateinit var mFbCloudDbRepos: FirebaseCloudDbRepository
    private lateinit var mFbCloudQueryRepos: FirebaseCloudQueryRepository
    private var mUpdateResult = MutableLiveData<FirebaseResult>()
    private var mQueryResult = MutableLiveData<FirebaseUserResult>()
    private lateinit var mSubscriptionUpdate: Subscription
    private lateinit var mSubscriptionQuery: Subscription

    override fun initViewModel(app: Application) {
        mFbCloudDbRepos = FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos()
        mFbCloudQueryRepos = FirebaseCloudQueryRepositoryFactory.getInstanceFbCloudQueryRepos()
    }

    fun inputCheck(it: DataUnitFb<UserDataUnitRemoteFb>): ReturnCode { //可以返回不同的值，从而精确告诉客户错误类型
        //val check = username.contains("@")//待改进，正确的正则表达式，注意顺序
        if (it.caseKey.imageLocalAddr == "" && it.remote.remoteImage.imageRemoteAddr == "") return ReturnCode_Err_Uri
        if (it.remote.remoteInfo.nickName.isEmpty()) return ReturnCode_Err_Nick_Name
        if (it.remote.remoteInfo.companyName.isEmpty()) return ReturnCode_Err_Company_Name
        if (it.remote.remoteInfo.restaurantName.isEmpty()) return ReturnCode_Err_Restaurant_Name
        if (it.remote.remoteInfo.restaurantAddr.isEmpty()) return ReturnCode_Err_Restaurant_Addr
        if (it.remote.remoteInfo.restaurantFloor.isEmpty()) return ReturnCode_Err_Restaurant_Floor
        if (it.remote.remoteInfo.cuisineName.isEmpty()) return ReturnCode_Err_Cuisine_Name
        if (it.remote.remoteInfo.breakfast.isEmpty()
            && it.remote.remoteInfo.lunch.isEmpty()
            && it.remote.remoteInfo.dinner.isEmpty()
        ) return ReturnCode_Err_Scope
        if (it.remote.remoteInfo.nickName.isEmpty()) return ReturnCode_Err_Square
        if (it.remote.remoteInfo.nickName.isEmpty()) return ReturnCode_Err_Table_Number
        if (it.remote.remoteInfo.nickName.isEmpty()) return ReturnCode_Err_Employee_Number
        return ReturnCode_Success
    }

    fun getUpdateResult(): LiveData<FirebaseResult> {
        return mUpdateResult
    }

    fun <UserDataUnitRemoteFb> updateToUser(localData: DataUnitFb<UserDataUnitRemoteFb>): LiveData<FirebaseResult> {

        val rxFlow = mFbCloudDbRepos.updateToUser(localData)

        rxFlow.subscribe(object : FlowableSubscriber<FirebaseResult> {
            //这里是接受数据
            override fun onSubscribe(s: Subscription) {
                s.request(Long.MAX_VALUE)
                mSubscriptionUpdate = s
            }

            override fun onNext(it: FirebaseResult) {
                handleOnNext(it)
            }

            override fun onError(t: Throwable) {
                //应该通知activity显示登录失败，但是如何通知呢？构造loadingError Resource，甚至可以进行细分，提升用户体验
                val x = errorFbData(t.message)
                handleOnError(x)
            }

            override fun onComplete() {
                mSubscriptionUpdate.cancel()
            }
        })

        return LiveDataReactiveStreams.fromPublisher(rxFlow)
    }

    private fun handleOnNext(it: FirebaseResult) {
        mUpdateResult.value = it
    }

    private fun handleOnError(it: FirebaseResult) {
        mUpdateResult.value = it
    }

    fun taskIsOngoing(): Boolean {
        return mFbCloudDbRepos.foodTaskIsOngoing()
    }

    override fun onCleared() {
        super.onCleared()
    }

    /******************************************************************************************************************/
    fun getQueryUserResult(): LiveData<FirebaseUserResult> {
        return mQueryResult
    }

    fun queryUserProfile(): LiveData<FirebaseUserResult> {

        val rxFlow = mFbCloudQueryRepos.getUserProfile()

        rxFlow.subscribe(object : FlowableSubscriber<FirebaseUserResult> {
            //这里是接受数据
            override fun onSubscribe(s: Subscription) {
                s.request(Long.MAX_VALUE)
                mSubscriptionQuery = s
            }

            override fun onNext(it: FirebaseUserResult) {
                handleQueryOnNext(it)
            }

            override fun onError(t: Throwable) {
                //应该通知activity显示登录失败，但是如何通知呢？构造loadingError Resource，甚至可以进行细分，提升用户体验
                val x = errorFbUserData(t.message)
                handleQueryOnError(x)
            }

            override fun onComplete() {
                mSubscriptionQuery.cancel()
            }
        })

        return LiveDataReactiveStreams.fromPublisher(rxFlow)
    }

    private fun handleQueryOnNext(it: FirebaseUserResult) {
        mQueryResult.value = it
    }

    private fun handleQueryOnError(it: FirebaseUserResult) {
        mQueryResult.value = it
    }
}