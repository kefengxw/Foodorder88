package com.foodorder.order.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.firebase.FirebaseAuthRepository
import com.foodorder.order.view.componet.AuthData
import com.google.firebase.auth.AuthResult
import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

abstract class BaseViewModelWithLogin(app: Application) : BaseViewModel(app) {

    private lateinit var mSubscription: Subscription
    private lateinit var mFbRepos: FirebaseAuthRepository
    private var mLoginResult = MutableLiveData<Resource<AuthResult>>()
    private var mUsername: String = ""
    private var mPassword: String = ""

    override fun initViewModel(app: Application) {
        mFbRepos = mInstanceApp.mInstanceFbAuthRepos
        initSubViewModel(app)
    }

    abstract fun initSubViewModel(app: Application)

    fun getLoginResult(): LiveData<Resource<AuthResult>> {
        return mLoginResult
    }

    fun inputCheck(username: String): Boolean {//可以返回不同的值，从而精确告诉客户错误类型
        //val check = username.contains("@")//待改进，正确的正则表达式
        //getLoginStatus()
        //if (!check) return
        return true
    }

    fun doLogin(auth: AuthData): LiveData<Resource<AuthResult>> {

        mUsername = auth.mUsername
        mPassword = auth.mPassword

        val rxFlow = mFbRepos.handleLoginByEmail(mUsername, mPassword)

        rxFlow.subscribe(object : FlowableSubscriber<Resource<AuthResult>> {
            //这里是接受数据
            override fun onSubscribe(s: Subscription) {
                s.request(Long.MAX_VALUE)
                mSubscription = s
            }

            override fun onNext(it: Resource<AuthResult>?) {
                handleOnNext(it)
            }

            override fun onError(t: Throwable) {
                //应该通知activity显示登录失败，但是如何通知呢？构造loadingError Resource，甚至可以进行细分，提升用户体验
            }

            override fun onComplete() {
                mSubscription.cancel()
            }
        })

        return LiveDataReactiveStreams.fromPublisher(rxFlow)
    }

    private fun handleOnNext(it: Resource<AuthResult>?) {
        mLoginResult.value = it
    }

    fun getUsername(): String = mUsername

    fun getPassword(): String = mPassword

    fun doLogOut() {
        mFbRepos.signOut()
        mUsername = ""
        mPassword = ""
    }
}