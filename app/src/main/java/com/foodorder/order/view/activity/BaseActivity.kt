package com.foodorder.order.view.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.foodorder.order.app.HomeApplication
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.model.data.GlideRequests

abstract class BaseActivity : AppCompatActivity() {//extends DaggerAppCompatActivity

    protected val mBaseCtx: Context = this
    protected val mBaseActivity: BaseActivity = this
    protected var mBaseGlide: GlideRequests? = null //can be re-write by sub-activity if needed
    //val mActivityCoroutineScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ButterKnife or improve the Dagger2, just reserved for expand.
        initOnCreate()
        initInjector()
        initViewModel()
        initView()
        initHomeAsUpButton()
        initListener()
        initLocalProcess()
    }

    fun getApplicationComponent(): HomeApplicationComponent {
        return (application as HomeApplication).getApplicationComponent()
    }

    private fun initOnCreate() {
        setContentView(initOnCreateLayoutResId())
        initOnCreateInitialize()
        setToolbar()
    }

    protected abstract fun initOnCreateInitialize()
    protected abstract fun initOnCreateLayoutResId(): Int
    protected abstract fun initOnCreateToolbar(): Toolbar?
    protected abstract fun initToolbarTitle(): String
    protected abstract fun initInjector()
    protected abstract fun initViewModel()
    protected abstract fun initView()
    protected abstract fun enableHomeAsUp(): Boolean
    protected abstract fun initListener()
    protected abstract fun initLocalProcess()

    private fun setToolbar() {
        val it = initOnCreateToolbar()
        it?.run {
            setSupportActionBar(it)
            it.title = initToolbarTitle()
            //value.subtitle = "nihao Testing"
        }
    }

    private fun initHomeAsUpButton() {
        if (enableHomeAsUp()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onStart() {
        super.onStart()
        initOnStart()
    }

    protected abstract fun initOnStart()
    protected abstract fun handleOnStop()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (enableHomeAsUp()) {
                    finish()
                }
            }
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        handleOnStop()
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        //mActivityCoroutineScope.cancel()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}