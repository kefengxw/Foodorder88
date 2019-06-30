package com.foodorder.order.view.fragment

import android.os.Bundle
import com.foodorder.order.R
import com.foodorder.order.model.data.SectionFragmentGuest

class MainCourseFrag : BaseFragmentWithItemClick() {

    //override lateinit var mRecyclerView: RecyclerView
    //override lateinit var mAdapter: OverviewItemAdapter

    companion object {
        fun newInstance(param: SectionFragmentGuest): BaseFragment {
            val it = MainCourseFrag()
            val args = Bundle().apply {

            }
            return it
        }
    }

    override fun providedFragmentLayoutId(): Int {
        return R.layout.main_course_layout
    }

//    override fun initRecyclerView(): RecyclerView {
//        return baseFragFindViewById(R.id.main_course_tab_layout)
//    }
//
//    override fun initRecyclerViewAdapter(): OverviewItemAdapter {
//        return getRecyclerViewAdapter()
//    }
//
//    override fun getGlide(): GlideRequests {
//        return GlideApp.with(this)
//    }
//
//    override fun getLayoutManager(): LinearLayoutManager {
//        return LinearLayoutManager(context)
//    }
//
//    override fun getQuery(): Query {
//        return mViewModel.getQueryWhereEqualTo(mCategory)
//    }
//
//    override fun doActionOnRecyclerViewItemClick(actionData: OverviewItem, position: Int) {
//        handleItemOrderOverview( , ,this)
//    }
//
//    override fun doActionOnRecyclerViewItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        //do nothing here
//    }
}