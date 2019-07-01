package com.foodorder.order.view.fragment

import android.os.Bundle
import android.view.View
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.getItemNumberDessert
import com.foodorder.order.model.data.SectionFragmentGuest

class DessertFrag : BaseFragmentWithItemOrder() {

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = DessertFrag()
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
        return R.layout.dessert_layout
    }

    override fun ProvidedItemArrayId(): IntArray {
        return intArrayOf(
            R.id.dessert_order_item_ov1,
            R.id.dessert_order_item_ov2,
            R.id.dessert_order_item_ov3,
            R.id.dessert_order_item_ov4,
            R.id.dessert_order_item_ov5,
            R.id.dessert_order_item_ov6,
            R.id.dessert_order_item_ov7,
            R.id.dessert_order_item_ov8
        )
    }

    override fun ProvidedItemNumber(): Int {
        return getItemNumberDessert()
    }
}