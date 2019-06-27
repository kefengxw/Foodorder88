package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalStatusConfiguration.getLoginUserEmail
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import com.foodorder.order.view.activity.RegisterActivity.Companion.startRegisterActivity
import com.foodorder.order.view.componet.AuthData
import com.foodorder.order.view.componet.InitialLoginHandle
import com.foodorder.order.view.componet.LoginDialog
import com.foodorder.order.view.componet.LoginDialog.Companion.startLoginDialog
import com.foodorder.order.view.componet.UnifiedButton
import com.foodorder.order.viewmodel.InitialViewModel
import kotlinx.android.synthetic.main.initial_activity.*

class InitialActivity : BaseActivity(), LoginDialog.LoginDialogListener {

    private lateinit var mRegisterBtn: UnifiedButton
    private lateinit var mLoginBtn: UnifiedButton
    private lateinit var mViewModel: InitialViewModel
    private lateinit var mSharedPre: SharedPreferences//可以移动到dagger中

    companion object {
        fun startInitialActivity(ctx: Context) {
            val intent = Intent(ctx, InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.initial_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return initial_tool_bar as Toolbar
    }

    override fun initInjector() {//TBD后续使用注入方式进行
        mSharedPre = SecuritySharedPreferencesFactory.getInstanceSSharedPre(mBaseCtx.applicationContext)
    }

    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this@InitialActivity).get(InitialViewModel::class.java)
        InitialLoginHandle(mBaseCtx, this@InitialActivity, this@InitialActivity, mViewModel)
    }

    override fun initView() {
        mRegisterBtn = initial_register
        mLoginBtn = initial_login
    }

    override fun initToolbarTitle(): String {
        return "Initial of App"
    }

    override fun enableHomeAsUp(): Boolean {
        return false
    }

    override fun initListener() {
        mRegisterBtn.setOnClickListener(registerListener)
        mLoginBtn.setOnClickListener(loginListener)
    }

    override fun initLocalProcess() {

        val username = mSharedPre.getString("username", "")
        val password = mSharedPre.getString("password", "")

        if ((username != "") && (password != "")) {
            ApplyLogin(username, password)
        }
    }

    override fun initOnStart() {

    }

    private val registerListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            startRegisterActivity(mBaseCtx)
        }
    }

    private val loginListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            startLoginDialog(supportFragmentManager, getLoginUserEmail())
        }
    }

    override fun ApplyLogin(username: String, password: String) {
        if (!mViewModel.inputCheck(username)) {
            showToast("Wrong Username!")
            return
        }
        mViewModel.doLogin(AuthData(username, password))
    }
}