package com.foodorder.order.view.fragment

import android.os.Bundle
import android.view.View
import com.foodorder.order.R
import com.foodorder.order.model.data.SectionFragmentManager
import com.foodorder.order.viewmodel.HomeViewModel

class ManagerTestFragment : BaseFragment() {

    companion object {
        fun newInstance(param: SectionFragmentManager): BaseFragment {
            val it = ManagerTestFragment()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun initViewComm(view: View) {

    }

    override fun initViewModelObserver(viewModel: HomeViewModel) {

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