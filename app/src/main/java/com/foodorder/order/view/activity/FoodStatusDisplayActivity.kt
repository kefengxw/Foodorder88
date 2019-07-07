package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.R
import kotlinx.android.synthetic.main.food_status_display.*


class FoodStatusDisplayActivity : BaseActivity() {

    companion object {
        fun startFoodStatusDisplayActivity(ctx: Context) {
            val intent = Intent(ctx, FoodStatusDisplayActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.food_status_display
    }

    override fun initOnCreateToolbar(): Toolbar {
        return food_status_display_tool_bar as Toolbar
    }

    override fun initInjector() {

    }

    override fun initViewModel() {

    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "food_status_display"
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
