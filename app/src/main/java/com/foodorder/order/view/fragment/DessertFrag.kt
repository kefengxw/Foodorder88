package com.foodorder.order.view.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideApp
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.model.data.SectionFragmentGuest
import com.foodorder.order.view.activity.BaseRecyclerView
import com.foodorder.order.view.adapter.FoodDisplayItemAdapter
import com.foodorder.order.view.adapter.FoodDisplayItemClick
import com.foodorder.order.view.adapter.FoodDisplayItemHolder
import com.foodorder.order.view.adapter.OverviewFoodItem
import com.foodorder.order.viewmodel.HomeViewModel
import com.google.firebase.firestore.Query

class DessertFrag : BaseFragment(),
    BaseRecyclerView<FoodDisplayItemHolder, FoodDisplayItemAdapter>,
    FoodDisplayItemClick {

    override lateinit var mRecyclerView: RecyclerView
    override lateinit var mAdapter: FoodDisplayItemAdapter

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

    override fun initViewModelObserver(viewModel: HomeViewModel) {
        //viewModel.getQuery()
    }

    override fun initViewListener(view: View) {

    }

    override fun initLocalProcess() {
        initRecycler()
    }

    override fun initOnStart() {
        startRecyclerViewListening()
    }

    override fun handleOnStop() {
        stopRecyclerViewListening()
    }

    override fun providedFragmentLayoutId(): Int {
        return R.layout.dessert_layout
    }

    private fun initRecycler() {
        mRecyclerView = initRecyclerView()
        mAdapter = initRecyclerViewAdapter()

        initializeRecyclerView()
    }

    override fun initRecyclerView(): RecyclerView {
        return baseFragFindViewById(R.id.dessert_tab_layout)
    }

    override fun createRecyclerViewAdapter(
        options: FirestoreRecyclerOptions<OverviewFoodItem>,
        glide: GlideRequests
    ): FoodDisplayItemAdapter {
        return FoodDisplayItemAdapter(options, glide)
    }

    override fun initRecyclerViewAdapter(): FoodDisplayItemAdapter {
        return getRecyclerViewAdapter()
    }

    override fun getGlide(): GlideRequests {
        return GlideApp.with(this)
    }

    override fun getLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(mBaseCtx)
    }

    override fun getQuery(): Query {
        //后续优化，需要考虑dagger
        return mViewModel.getQuery()
    }

    override fun doActionOnRecyclerViewItemClick(itemView: View, actionData: OverviewFoodItem, position: Int) {
        handleDisplayItemClick(itemView, activity!!)
    }

    override fun doActionOnRecyclerViewItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //do nothing here
    }
}