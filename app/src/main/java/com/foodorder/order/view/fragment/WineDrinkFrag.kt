package com.foodorder.order.view.fragment

import android.os.Bundle
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.getItemNumberWineDrink
import com.foodorder.order.model.data.SectionFragmentGuest

class WineDrinkFrag : BaseFragmentWithItemOrder() {

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = WineDrinkFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun ProvidedFragmentLayoutId(): Int {
        return R.layout.wine_drink_layout
    }

    override fun ProvidedItemArrayId(): IntArray {
        return intArrayOf(
            R.id.wine_drink_order_item_ov1,
            R.id.wine_drink_order_item_ov2,
            R.id.wine_drink_order_item_ov3,
            R.id.wine_drink_order_item_ov4,
            R.id.wine_drink_order_item_ov5,
            R.id.wine_drink_order_item_ov6,
            R.id.wine_drink_order_item_ov7,
            R.id.wine_drink_order_item_ov8
        )
    }

    override fun ProvidedItemNumber(): Int {
        return getItemNumberWineDrink()
    }
}