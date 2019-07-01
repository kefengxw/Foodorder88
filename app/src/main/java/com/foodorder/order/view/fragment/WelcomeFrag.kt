package com.foodorder.order.view.fragment

import android.os.Bundle
import android.view.View
import com.foodorder.order.R
import com.foodorder.order.model.data.SectionFragmentGuest

class WelcomeFrag : BaseFragment() {

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = WelcomeFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun initViewComm(view: View) {

    }

    override fun initViewListener(view: View) {

    }

    override fun initLocalProcess() {

    }

    override fun initOnStart() {

    }

    override fun handleOnStop() {

    }

    override fun providedFragmentLayoutId(): Int {
        return R.layout.welcome_layout
    }
}