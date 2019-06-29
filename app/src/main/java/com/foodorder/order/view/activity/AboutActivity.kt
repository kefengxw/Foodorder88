package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.R
import kotlinx.android.synthetic.main.about_activity.*


class AboutActivity : BaseActivity() {

    companion object {
        fun startAboutActivity(ctx: Context) {
            val intent = Intent(ctx, AboutActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.about_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return about_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "About"
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
