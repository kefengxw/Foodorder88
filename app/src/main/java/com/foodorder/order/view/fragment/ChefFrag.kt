package com.foodorder.order.view.fragment

import android.os.Bundle
import com.foodorder.order.R
import com.foodorder.order.model.data.SectionFragmentChef

class ChefFrag : BaseFragment() {

    companion object {
        fun newInstance(param: SectionFragmentChef): BaseFragment {
            val it = WelcomeFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun providedFragmentLayoutId(): Int {
        return R.layout.welcome_layout
    }
}