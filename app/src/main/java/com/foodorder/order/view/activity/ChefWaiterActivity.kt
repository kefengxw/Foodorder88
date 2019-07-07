package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.R
import kotlinx.android.synthetic.main.chef_waiter_activity.*


class ChefWaiterActivity : BaseActivity() {

    companion object {
        fun startChefWaiterActivity(ctx: Context) {
            val intent = Intent(ctx, ChefWaiterActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.chef_waiter_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return chef_waiter_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "chef_waiter"
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
