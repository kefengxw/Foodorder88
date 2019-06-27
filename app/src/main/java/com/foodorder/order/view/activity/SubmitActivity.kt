package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.R
import kotlinx.android.synthetic.main.submit_activity.*


class SubmitActivity : BaseActivity() {

    companion object {
        fun startSubmitActivity(ctx: Context) {
            val intent = Intent(ctx, SubmitActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.submit_activity)
        //setSupportActionBar(submit_tool_bar as Toolbar)

        //透明化处理statusbar
//        val window = window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.TRANSPARENT
    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.submit_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return submit_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "Submit"
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
}