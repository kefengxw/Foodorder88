package com.foodorder.order.view.fragment

import android.os.Bundle
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.getItemNumberStarter
import com.foodorder.order.model.data.SectionFragmentGuest
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView

class StarterFrag : BaseFragmentWithItemOrder() {

    private lateinit var mItemImage: UnifiedImageView
    private lateinit var mItemAdd: UnifiedImageView
    private lateinit var mItemRemove: UnifiedImageView
    private lateinit var mItemTextNumber: UnifiedTextView

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = StarterFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun ProvidedFragmentLayoutId(): Int {
        return R.layout.starter_layout
    }

    override fun ProvidedItemArrayId(): IntArray {
        return intArrayOf(
            R.id.starter_order_item_ov1,
            R.id.starter_order_item_ov2,
            R.id.starter_order_item_ov3,
            R.id.starter_order_item_ov4,
            R.id.starter_order_item_ov5,
            R.id.starter_order_item_ov6,
            R.id.starter_order_item_ov7,
            R.id.starter_order_item_ov8
        )
    }

    override fun ProvidedItemNumber(): Int {
        return getItemNumberStarter()
    }
}