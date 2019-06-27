package com.foodorder.order.view.fragment

import android.os.Bundle
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

    override fun ProvidedFragmentLayoutId(): Int {
        return R.layout.welcome_layout
    }
}