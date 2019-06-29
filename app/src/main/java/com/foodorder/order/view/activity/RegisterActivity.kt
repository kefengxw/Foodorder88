package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.view.componet.AuthData
import com.foodorder.order.view.componet.RegisterHandle
import com.foodorder.order.view.componet.UnifiedButton
import com.foodorder.order.view.componet.UnifiedEditText
import com.foodorder.order.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : BaseActivity() {

    private lateinit var mRegisterSubmitBtn: UnifiedButton
    private lateinit var mUsernameBtn: UnifiedEditText
    private lateinit var mPasswordBtn: UnifiedEditText
    private lateinit var mViewModel: RegisterViewModel
    private var mUsername: String = ""
    private var mPassword: String = ""

    companion object {
        fun startRegisterActivity(ctx: Context) {
            val intent = Intent(ctx, RegisterActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.register_activity
    }

    override fun initOnCreateToolbar(): Toolbar? {
        return register_tool_bar as Toolbar
    }

    override fun initInjector() {
        mViewModel = ViewModelProviders.of(this@RegisterActivity).get(RegisterViewModel::class.java)
        RegisterHandle(mBaseCtx, this@RegisterActivity, this@RegisterActivity, mViewModel)
    }

    override fun initViewModel() {

    }

    override fun initView() {
        mRegisterSubmitBtn = register_submit
        mUsernameBtn = edit_text_username
        mPasswordBtn = edit_text_password
    }

    override fun initToolbarTitle(): String {
        return "Register New User"
    }

    override fun enableHomeAsUp(): Boolean {
        return true
    }

    override fun initListener() {
        mRegisterSubmitBtn.setOnClickListener(registerSubmitListener)
        //mUsernameBtn.setOnTouchListener(usernameListener)实现触碰，离开等事件
        //addTextChangedListener 输入过程中的监听
    }

    override fun initLocalProcess() {

    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }

    private val usernameListener = object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    private val registerSubmitListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            showToast("registerSubmitListener")

            //也可以在这里进行检查
            mUsername = edit_text_username.text.toString()
            mPassword = edit_text_password.text.toString()

            if (!mViewModel.inputCheck(mUsername)) {
                showToast("Wrong Username!")
                return
            }
            mViewModel.doRegister(AuthData(mUsername, mPassword))
        }
    }
}