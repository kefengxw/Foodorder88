package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foodorder.order.model.firebase.FirebaseCloudDbRepository
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.model.firebase.FirebaseResult
import com.foodorder.order.view.adapter.OverviewItem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import org.reactivestreams.Subscription

class UploadOverviewModel(app: Application) : BaseViewModel(app) {

    private lateinit var mFbCloudDbRepos: FirebaseCloudDbRepository
    private var mUploadResult = MutableLiveData<FirebaseResult>()
    private lateinit var mSubscription: Subscription


    override fun initViewModel(app: Application) {
        mFbCloudDbRepos = FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos()
    }

    fun getQueryOrderByKey(): Query {
        return mFbCloudDbRepos.getFoodQueryOrderByKey()
    }

    fun getQueryWhereEqualTo(it: String): Query {
        return mFbCloudDbRepos.getFoodQueryWhereEqualTo(it)
    }

    fun getUploadResult(): LiveData<FirebaseResult> {
        return mUploadResult
    }

    fun inputCheck(username: String): Boolean { //可以返回不同的值，从而精确告诉客户错误类型
        //val check = username.contains("@")//待改进，正确的正则表达式
        //getLoginStatus()
        //if (!check) return
        return true
    }

//    fun uploadFood(uploadLocalFood: UploadLocalFoodDataUnit): LiveData<FirebaseResult> {
//
//        val rxFlow = mFbCloudDbRepos.updateUser(uploadLocalFood)
//
//        rxFlow.subscribe(object : FlowableSubscriber<FirebaseResult> {
//            //这里是接受数据
//            override fun onSubscribe(s: Subscription) {
//                s.request(Long.MAX_VALUE)
//                mSubscription = s
//            }
//
//            override fun onNext(it: FirebaseResult) {
//                handleOnNext(it)
//            }
//
//            override fun onError(t: Throwable) {
//                //应该通知activity显示登录失败，但是如何通知呢？构造loadingError Resource，甚至可以进行细分，提升用户体验
//                val x = errorFbData(t.message)
//                handleOnError(x)
//            }
//
//            override fun onComplete() {
//                mSubscription.cancel()
//            }
//        })
//
//        return LiveDataReactiveStreams.fromPublisher(rxFlow)
//    }

    private fun handleOnNext(it: FirebaseResult) {
        mUploadResult.value = it
    }

    private fun handleOnError(it: FirebaseResult) {
        mUploadResult.value = it
    }

    fun deleteFood(docRef: DocumentReference, item: OverviewItem) {
        mFbCloudDbRepos.deleteFood(docRef, item)
    }

    fun taskIsOngoing(): Boolean {
        return mFbCloudDbRepos.foodTaskIsOngoing()
    }

    override fun onCleared() {
        super.onCleared()
    }
}