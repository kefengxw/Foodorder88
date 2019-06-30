package com.foodorder.order.view.componet

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.foodorder.order.model.data.InternalStatusConfiguration.updateLoginUser
import com.foodorder.order.model.data.InternalStatusConfiguration.updateLoginUserId
import com.foodorder.order.model.data.Phase
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.data.Status
import com.foodorder.order.model.data.Status.*
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.model.firebase.FirebaseCloudQueryRepositoryFactory
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import com.foodorder.order.util.UtilInternalFunction.saveUserDataForNextOpen
import com.foodorder.order.view.activity.BaseActivity
import com.foodorder.order.view.activity.HomeActivity
import com.foodorder.order.view.activity.HomeActivity.Companion.startHomeActivity
import com.foodorder.order.view.activity.InitialActivity
import com.foodorder.order.view.activity.SettingActivity.Companion.startSettingActivity
import com.foodorder.order.viewmodel.BaseViewModelWithLogin
import com.google.firebase.auth.AuthResult

abstract class BaseLoginHandle(
    val mCtx: Context,
    val mActivity: BaseActivity,
    val mOwner: LifecycleOwner,
    val mViewModel: BaseViewModelWithLogin
) {
    //这个可以和LoginHandle一起考虑，抽象一下；改进点
    private val mProgressInfo = ProgressInfo(mActivity)
    private lateinit var mSharedPre: SharedPreferences

    private val observerLoginResult = object : Observer<Resource<AuthResult>> {
        //start to login into firebase, show status of network or progress, show successful result or failed result
        override fun onChanged(it: Resource<AuthResult>) = when (it.mStatus) {
            LOADING, ERROR -> updateLoginInfoAndStatus("", it.mStatus)
            SUCCESS -> handleLoginSuccess(it)
        }
    }

    init {
        initInjector()
        initViewModel()
        initView()
        initListener()
        initLocalProcess()
    }

    private fun initInjector() {//TBD后续使用注入的方式进行
        mSharedPre = SecuritySharedPreferencesFactory.getInstanceSSharedPre(mCtx.applicationContext)
    }

    private fun initViewModel() {
        mViewModel.getLoginResult().observe(mOwner, observerLoginResult)
    }

    private fun initView() {
        mProgressInfo.progressInfoInit(provideProgressId())
    }

    private fun initListener() {

    }

    private fun initLocalProcess() {
        mProgressInfo.progressInfoSetText(provideProgressText(), provideProgressFailText())
    }

    private fun updateLoginInfoAndStatus(userId: String, it: Status) {
        updateLoginUserId(userId)
        setNetworkState(it, Phase.AUTH_LOGIN)
    }

    private fun setNetworkState(x: Status, ph: Phase) {
        when (ph) {
            Phase.AUTH_LOGIN -> {
                mProgressInfo.progressInfoSetStatus(x)
            }
        }
    }

    fun handleLoginSuccess(it: Resource<AuthResult>) {

        doActionOnLoginSuccess(it)

        //这个动作，建议移动到子类去做，比较合适
        val currentUser = it.mData!!.user
        updateLoginUser(currentUser)
        mActivity.showToast("Login success - ${currentUser.uid}")
        updateLoginInfoAndStatus(currentUser.uid, it.mStatus)//save to app now
        //updateProfile() 1.后续可以增加编辑餐馆信息的页面
        saveUserDataForNextOpen(mSharedPre, mViewModel.getUsername(), mViewModel.getPassword())
        when (mActivity) {
            is HomeActivity -> {
                startSettingActivity(mCtx)
            }
            is InitialActivity -> {
                startHomeActivity(mCtx)
                mActivity.finish()
            }
        }
        //初始化一次，后续可以考虑移动，但是好像应该放在这里
        //同时，在logout的时候，需要除去这个
        //可以移动到进入setting页面的时候，才考虑启动这个Instance
        FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos()
        FirebaseCloudQueryRepositoryFactory.getInstanceFbCloudQueryRepos()
    }

    abstract fun provideProgressId(): Int
    abstract fun provideProgressText(): String
    abstract fun provideProgressFailText(): String
    abstract fun doActionOnLoginSuccess(it: Resource<AuthResult>)
}