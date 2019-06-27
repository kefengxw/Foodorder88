package com.foodorder.order.view.componet

import android.view.View
import android.widget.ProgressBar
import com.foodorder.order.R
import com.foodorder.order.model.data.Status
import com.foodorder.order.view.activity.BaseActivity

class ProgressInfo(val mActivity: BaseActivity) {

    lateinit var mProgress: View
    lateinit var mProgressBar: ProgressBar
    lateinit var mProgressText: UnifiedTextView
    lateinit var mProgressFailText: UnifiedTextView

    fun progressInfoInit(x: Int) {
        mProgress = mActivity.findViewById(x)
        mProgressBar = mActivity.findViewById(R.id.progress_bar)
        mProgressText = mActivity.findViewById(R.id.progress_text)
        mProgressFailText = mActivity.findViewById(R.id.progress_fail_text)
    }

    fun progressInfoSetText(x: String, y: String) {
        mProgressText.text = x
        mProgressFailText.text = y
    }

    fun progressInfoSetStatus(x: Status) {
        if (x == Status.SUCCESS) {
            mProgress.visibility = View.GONE
        } else {
            mProgress.visibility = View.VISIBLE
            mProgressBar.visibility = if (x == Status.LOADING) View.VISIBLE else View.INVISIBLE
            mProgressText.visibility = if (x == Status.LOADING) View.VISIBLE else View.INVISIBLE
            mProgressFailText.visibility = if (x == Status.ERROR) View.VISIBLE else View.INVISIBLE
        }
    }
}