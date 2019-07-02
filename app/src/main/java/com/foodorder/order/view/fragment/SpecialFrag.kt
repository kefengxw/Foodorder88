package com.foodorder.order.view.fragment

import android.os.Bundle
import android.view.View
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.getItemNumberSpecial
import com.foodorder.order.model.data.SectionFragmentGuest
import com.foodorder.order.viewmodel.HomeViewModel

class SpecialFrag : BaseFragmentWithItemOrder() {

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = SpecialFrag()
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
        return R.layout.special_layout
    }

    override fun ProvidedItemArrayId(): IntArray {
        return intArrayOf(
            R.id.special_order_item_ov1,
            R.id.special_order_item_ov2,
            R.id.special_order_item_ov3,
            R.id.special_order_item_ov4,
            R.id.special_order_item_ov5,
            R.id.special_order_item_ov6,
            R.id.special_order_item_ov7,
            R.id.special_order_item_ov8
        )
    }

    override fun ProvidedItemNumber(): Int {
        return getItemNumberSpecial()
    }
}