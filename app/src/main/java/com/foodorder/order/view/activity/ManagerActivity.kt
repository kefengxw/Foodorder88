package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.R
import kotlinx.android.synthetic.main.about_activity.*
import kotlinx.android.synthetic.main.manager_activity.*


class ManagerActivity : BaseActivity() {

    companion object {
        fun startManagerActivity(ctx: Context) {
            val intent = Intent(ctx, ManagerActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.manager_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return manager_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "Manager"
    }

    override fun enableHomeAsUp(): Boolean {
        return true
    }

    override fun initListener() {

    }

    override fun initLocalProcess() {

    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }
}
