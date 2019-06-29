package com.foodorder.order.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideApp
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.activity.UploadEditActivity.Companion.startUploadEditActivity
import com.foodorder.order.view.adapter.OverviewItem
import com.foodorder.order.view.adapter.OverviewItemAdapter
import com.foodorder.order.view.componet.UnifiedButton
import com.foodorder.order.viewmodel.UploadOverviewModel
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.upload_overview_activity.*

class UploadOverviewActivity : BaseActivity(), BaseRecyclerView {

    private lateinit var mViewModel: UploadOverviewModel
    private lateinit var mAddBtn: UnifiedButton
    override lateinit var mRecyclerView: RecyclerView
    override lateinit var mAdapter: OverviewItemAdapter
    private var mCategory: String = ""

    companion object {
        fun startUploadOverviewActivity(ctx: Context) {
            val intent = Intent(ctx, UploadOverviewActivity::class.java)
            ctx.startActivity(intent)
        }

        fun startUploadOverviewActivity(ctx: Context, cate: String) {
            val intent = Intent(ctx, UploadOverviewActivity::class.java)
            intent.putExtra("category", cate)
            ctx.startActivity(intent)
        }

        fun decodeCategoryData(it: UploadOverviewActivity): String {
            val x = it.intent.getStringExtra("category")//need to check null ?
            return x
        }
    }

    override fun initOnCreateInitialize() {
        mCategory = getFoodCategory()
    }

    override fun initOnCreateLayoutResId(): Int {
        return R.layout.upload_overview_activity
    }

    override fun initOnCreateToolbar(): Toolbar? {
        return upload_overview_tool_bar as Toolbar
    }

    override fun initToolbarTitle(): String {
        return ("Upload Overview - " + this.mCategory)
    }

    override fun initInjector() {

    }

    override fun initViewModel() {
        mViewModel = ViewModelProviders.of(this@UploadOverviewActivity).get(UploadOverviewModel::class.java)
    }

    override fun initView() {
        mAddBtn = upload_overview_add
    }

    override fun initListener() {
        mAddBtn.setOnClickListener(addBtnListener)
    }

    override fun initLocalProcess() {
        initRecycler()
    }

    override fun enableHomeAsUp(): Boolean {
        return true
    }

    override fun initOnStart() {
        startRecyclerViewListening()
    }

    override fun handleOnStop() {
        stopRecyclerViewListening()
    }

    private val addBtnListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            startUploadEditActivity(mBaseCtx, mCategory)
        }
    }

    private fun getFoodCategory(): String {
        return decodeCategoryData(this@UploadOverviewActivity)
    }

    private fun initRecycler() {
        mRecyclerView = initRecyclerView()
        mAdapter = initRecyclerViewAdapter()

        initializeRecyclerView()
    }

    override fun initRecyclerView(): RecyclerView {
        return findViewById(R.id.upload_overview_recycler)
    }

    override fun initRecyclerViewAdapter(): OverviewItemAdapter {
        return getRecyclerViewAdapter()
    }

    override fun getGlide(): GlideRequests {
        return GlideApp.with(this)
    }

    override fun getLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(this)
    }

    override fun getQuery(): Query {
        //return mViewModel.getQueryOrderByKey()
        return mViewModel.getQueryWhereEqualTo(mCategory)
    }

    override fun doActionOnRecyclerViewItemClick(actionData: OverviewItem, position: Int) {
        showToast("Item onItemClick! the Position: $position")
        startUploadEditActivity(mBaseCtx, actionData)
    }

    override fun doActionOnRecyclerViewItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val x = viewHolder.adapterPosition
        showToast("Item($x) deleted!")
        mViewModel.deleteFood(mAdapter.getSnapshotRef(x), mAdapter.getItem(x))
    }
}