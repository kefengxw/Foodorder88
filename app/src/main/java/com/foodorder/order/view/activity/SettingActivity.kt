package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.foodorder.order.R
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.model.firebase.FirebaseCloudQueryRepositoryFactory
import com.foodorder.order.util.SecuritySharedPreferencesFactory
import com.foodorder.order.util.UtilInternalFunction.saveUserDataForNextOpen
import com.foodorder.order.view.activity.InitialActivity.Companion.startInitialActivity
import com.foodorder.order.view.componet.LogoutDialog
import com.foodorder.order.view.fragment.SettingFragment
import com.foodorder.order.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.setting_activity.*

class SettingActivity : BaseActivity(),
    LogoutDialog.LogoutDialogListener {

    private lateinit var mViewModel: SettingViewModel
    private lateinit var mSharedPre: SharedPreferences//可以移动到dagger中

    companion object {
        fun startSettingActivity(ctx: Context) {
            val intent = Intent(ctx, SettingActivity::class.java)
            ctx.startActivity(intent)
        }
    }

    override fun initOnCreateInitialize() {

    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.setting_activity
    }

    override fun initOnCreateToolbar(): Toolbar {
        return setting_tool_bar as Toolbar
    }

    override fun initInjector() {
        mSharedPre = SecuritySharedPreferencesFactory.getInstanceSSharedPre(mBaseCtx.applicationContext)
    }

    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this@SettingActivity).get(SettingViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initToolbarTitle(): String {
        return "Setting"
    }

    override fun enableHomeAsUp(): Boolean {
        return true
    }

    override fun initListener() {

    }

    override fun initLocalProcess() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting, SettingFragment())
            .commit()
    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }

    override fun ApplyLogout() {
        FirebaseCloudDbRepositoryFactory.destroyInstanceFbCloudDbRepos()
        FirebaseCloudQueryRepositoryFactory.destroyInstanceFbCloudQueryRepos()

        mViewModel.doLogOut()

        saveUserDataForNextOpen(mSharedPre, mViewModel.getUsername(), mViewModel.getPassword())

        finish()
        startInitialActivity(mBaseCtx)
        showToast("Logout success!")
    }
}