package com.foodorder.order.view.fragment

import android.os.Bundle
import com.foodorder.order.R
import com.foodorder.order.model.data.InternalDataConfiguration.getItemNumberMainCourse
import com.foodorder.order.model.data.SectionFragmentGuest

class MainCourseFrag : BaseFragmentWithItemOrder() {

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = MainCourseFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun ProvidedFragmentLayoutId(): Int {
        return R.layout.main_course_layout
    }

    override fun ProvidedItemArrayId(): IntArray {
        return intArrayOf(
            R.id.main_course_order_item_ov1,
            R.id.main_course_order_item_ov2,
            R.id.main_course_order_item_ov3,
            R.id.main_course_order_item_ov4,
            R.id.main_course_order_item_ov5,
            R.id.main_course_order_item_ov6,
            R.id.main_course_order_item_ov7,
            R.id.main_course_order_item_ov8
        )
    }

    override fun ProvidedItemNumber(): Int {
        return getItemNumberMainCourse()
    }
}