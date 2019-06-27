package com.foodorder.order.view.componet

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.foodorder.order.R
import com.foodorder.order.model.data.Resource
import com.foodorder.order.view.activity.BaseActivity
import com.foodorder.order.viewmodel.BaseViewModelWithLogin
import com.google.firebase.auth.AuthResult

class InitialLoginHandle(
    ctx: Context,
    activity: BaseActivity,
    owner: LifecycleOwner,
    viewModel: BaseViewModelWithLogin
) : BaseLoginHandle(ctx, activity, owner, viewModel) {

    override fun provideProgressId(): Int {
        return R.id.initial_progress_info
    }

    override fun provideProgressText(): String {
        return "Login now..."
    }

    override fun provideProgressFailText(): String {
        return "Failed to auto login..."
    }

    override fun doActionOnLoginSuccess(it: Resource<AuthResult>) {

    }
}