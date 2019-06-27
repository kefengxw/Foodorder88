package com.foodorder.order.view.componet

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import com.foodorder.order.view.activity.BaseActivity

class UnifiedSpinnerHandle(val mActivity: BaseActivity, list: ArrayList<UnifiedSpinnerItem>) {

    lateinit var mCategorySpinner: UnifiedSpinnerView
    var mSpinnerList = list//ArrayList<UnifiedSpinnerItem>()
    private var mListener: HandleSpinnerResult
    var position: Int//后续待改进

    init {
        mListener = mActivity as HandleSpinnerResult
        position = 2
    }

    fun spinnerHandleInit(id: Int) {
        mCategorySpinner = mActivity.findViewById(id)
        localProcess()
    }

    private fun localProcess() {
        initSpinner()
    }

    private fun initSpinner() {

        mCategorySpinner.adapter = UnifiedSpinnerAdapter(mActivity, mSpinnerList)
        mCategorySpinner.onItemSelectedListener = spinnerListener

        mCategorySpinner.setSelection(position, true)//positoin从mCategory来
        mCategorySpinner.gravity = Gravity.CENTER
    }

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            //nothing to do
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent!!.getItemAtPosition(position) as UnifiedSpinnerItem
            //showToast("Category Spinner ---${item}--- is selected")
            //mCategory = item.mCategory
            mListener.spinnerResult(this@UnifiedSpinnerHandle, item)
        }
    }

    interface HandleSpinnerResult {
        //if (type == )用来区分可能存在2个spinner的情况
        fun spinnerResult(type: UnifiedSpinnerHandle, item: UnifiedSpinnerItem)
    }
}