package com.foodorder.order.view.componet

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalStatusConfiguration.updateLoginUser
import com.foodorder.order.model.data.InternalStatusConfiguration.updateLoginUserId
import com.foodorder.order.model.data.Phase
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.data.Status
import com.foodorder.order.model.data.Status.*
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import com.foodorder.order.util.UtilInternalFunction.saveUserDataForNextOpen
import com.foodorder.order.view.activity.BaseActivity
import com.foodorder.order.view.activity.HomeActivity.Companion.startHomeActivity
import com.foodorder.order.viewmodel.RegisterViewModel
import com.google.firebase.auth.AuthResult

class RegisterHandle(
    val mCtx: Context,
    val mActivity: BaseActivity,
    val mOwner: LifecycleOwner,
    val mViewModel: RegisterViewModel
) {

    private lateinit var mSharedPre: SharedPreferences
    private val mProgressInfo = ProgressInfo(mActivity)

    private val observerResult = object : Observer<Resource<AuthResult>> {
        //start to login into firebase, show status of network or progress, show successful result or failed result
        override fun onChanged(it: Resource<AuthResult>) = when (it.mStatus) {
            LOADING, ERROR -> updateRegisterInfoAndStatus("", it.mStatus)
            SUCCESS -> handleRegisterSuccess(it)
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
        mViewModel.getRegisterResult().observe(mOwner, observerResult)
    }

    private fun initView() {
        mProgressInfo.progressInfoInit(R.id.register_progress_info)
    }

    private fun initListener() {

    }

    private fun initLocalProcess() {
        val x = "Register now..."
        val y = "Failed to register new user..."
        mProgressInfo.progressInfoSetText(x, y)
    }

    private fun updateRegisterInfoAndStatus(userId: String, it: Status) {
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

    fun handleRegisterSuccess(it: Resource<AuthResult>) {
        val currentUser = it.mData!!.user
        updateLoginUser(currentUser)
        mActivity.showToast("Register success - ${currentUser.uid}")
        updateRegisterInfoAndStatus(currentUser.uid, it.mStatus)//save to app now
        //updateProfile() 1.后续可以增加编辑餐馆信息的页面
        saveUserDataForNextOpen(mSharedPre, mViewModel.getUsername(), mViewModel.getPassword())
        startHomeActivity(mCtx)
        mActivity.finish()
/*        //when (mActivity)
        {
//            is HomeActivity -> {
//                startSettingActivity(mCtx)
//            }
//            is InitialActivity -> {
//                startHomeActivity(mCtx)
//                mActivity.finish()
//            }
        }*/
        //初始化一次，后续可以考虑移动，但是好像应该放在这里
        //同时，在logout的时候，需要除去这个
        //可以移动到进入setting页面的时候，才考虑启动这个Instance
        FirebaseCloudDbRepositoryFactory.getInstanceFbCloudDbRepos()
    }
}