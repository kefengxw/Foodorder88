package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.foodorder.order.model.data.ReturnCode
import com.foodorder.order.model.data.ReturnCode.*
import com.foodorder.order.model.firebase.FirebaseCloudDbRepository
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.model.firebase.FirebaseResult
import com.foodorder.order.model.firebase.FirebaseResult.Companion.errorFbData
import com.foodorder.order.view.componet.UpdateLocalUserDataUnit
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

class EditProfileViewModel(app: Application) : BaseViewModel(app) {

    private lateinit var mFbCloudDbRepos: FirebaseCloudDbRepository
    private var mUploadResult = MutableLiveData<FirebaseResult>()
    private lateinit var mSubscription: Subscription

    override fun initViewModel(app: Application) {
        mFbCloudDbRepos = FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos()
    }

    fun inputCheck(it: UpdateLocalUserDataUnit): ReturnCode { //可以返回不同的值，从而精确告诉客户错误类型
        //val check = username.contains("@")//待改进，正确的正则表达式，注意顺序
        if (it.imageLocalAddr == "" && it.updateRemoteUser.imageRemoteAddr == "") return ReturnCode_Err_Uri
        if (it.updateRemoteUser.nickName.isEmpty()) return ReturnCode_Err_Nick_Name
        if (it.updateRemoteUser.companyName.isEmpty()) return ReturnCode_Err_Company_Name
        if (it.updateRemoteUser.restaurantName.isEmpty()) return ReturnCode_Err_Restaurant_Name
        if (it.updateRemoteUser.restaurantAddr.isEmpty()) return ReturnCode_Err_Restaurant_Addr
        if (it.updateRemoteUser.restaurantFloor.isEmpty()) return ReturnCode_Err_Restaurant_Floor
        if (it.updateRemoteUser.cuisineName.isEmpty()) return ReturnCode_Err_Cuisine_Name
        if (it.updateRemoteUser.breakfast.isEmpty()
            && it.updateRemoteUser.lunch.isEmpty()
            && it.updateRemoteUser.dinner.isEmpty()) return ReturnCode_Err_Scope
        if (it.updateRemoteUser.nickName.isEmpty()) return ReturnCode_Err_Square
        if (it.updateRemoteUser.nickName.isEmpty()) return ReturnCode_Err_Table_Number
        if (it.updateRemoteUser.nickName.isEmpty()) return ReturnCode_Err_Employee_Number
        return ReturnCode_Success
    }


    fun getUpdateResult(): LiveData<FirebaseResult> {
        return mUploadResult
    }

    fun updateUser(localUser: UpdateLocalUserDataUnit): LiveData<FirebaseResult> {

        val rxFlow = mFbCloudDbRepos.updateUser(localUser)

        rxFlow.subscribe(object : FlowableSubscriber<FirebaseResult> {
            //这里是接受数据
            override fun onSubscribe(s: Subscription) {
                s.request(Long.MAX_VALUE)
                mSubscription = s
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
                mSubscription.cancel()
            }
        })

        return LiveDataReactiveStreams.fromPublisher(rxFlow)
    }

    private fun handleOnNext(it: FirebaseResult) {
        mUploadResult.value = it
    }

    private fun handleOnError(it: FirebaseResult) {
        mUploadResult.value = it
    }

    fun taskIsOngoing(): Boolean {
        return mFbCloudDbRepos.foodTaskIsOngoing()
    }

    override fun onCleared() {
        super.onCleared()
    }
}