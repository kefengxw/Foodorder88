package com.foodorder.order.view.activity

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.adapter.OverviewItem
import com.foodorder.order.view.adapter.OverviewItemAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

interface BaseRecyclerView {

    //can be improved more, use <T> to replace OverviewItem
    var mRecyclerView: RecyclerView
    var mAdapter: OverviewItemAdapter

    fun initRecyclerView(): RecyclerView
    fun initRecyclerViewAdapter(): OverviewItemAdapter
    fun getGlide(): GlideRequests
    fun getLayoutManager(): LinearLayoutManager
    fun getQuery(): Query
    fun doActionOnRecyclerViewItemClick(actionData: OverviewItem, position: Int)
    fun doActionOnRecyclerViewItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)

    fun initializeRecyclerView() {
        initLocalProcess()
    }

    fun startRecyclerViewListening() {
        mAdapter.startListening()
    }

    fun stopRecyclerViewListening() {
        mAdapter.stopListening()
    }

    private fun initLocalProcess() {

        val itemClick = object : OverviewItemAdapter.OnItemClickInterface {
            override fun onItemClick(snapshot: DocumentSnapshot, position: Int) {
                val it = getItemDataUnit(snapshot)
                doActionOnRecyclerViewItemClick(it, position)
            }
        }

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(
            0, (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                view: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                doActionOnRecyclerViewItemSwiped(viewHolder, direction)
            }
        }

        mAdapter.setOnItemClick(itemClick)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = getLayoutManager()
        mRecyclerView.adapter = mAdapter

        object : ItemTouchHelper(itemSwipe) {}.attachToRecyclerView(mRecyclerView)
    }

    private fun getFirebaseOptions(convert: SnapshotParser<OverviewItem>): FirestoreRecyclerOptions<OverviewItem> {

        val query = getQuery()
        val options = FirestoreRecyclerOptions.Builder<OverviewItem>()
            .setQuery(query, convert)
            .build()

        return options
    }

    fun getRecyclerViewAdapter(): OverviewItemAdapter {

        val convert = object : SnapshotParser<OverviewItem> {
            override fun parseSnapshot(snapshot: DocumentSnapshot): OverviewItem {
                return getItemDataUnit(snapshot)
            }
        }
        val glide = getGlide()
        val options = getFirebaseOptions(convert)

        return OverviewItemAdapter(options, glide)
    }

    fun getItemDataUnit(snapshot: DocumentSnapshot): OverviewItem {
        //snapshot.reference
        //也可以放到子类中进行处理，暂时放在这里，因为OverviewItem是一个统一的，如果不同的话，就需要考虑不同子类分开
        var x = snapshot.getString("uniqueId")
        var y = snapshot.getString("name")
        var z = snapshot.getString("imageRemoteAddr")
        var l = snapshot.getString("imageRemotePath")
        var m = snapshot.getString("price")
        var n = snapshot.getString("description")
        var o = snapshot.getString("category")

        if (x == null) x = ""
        if (y == null) y = ""
        if (z == null) z = ""
        if (l == null) l = ""
        if (m == null) m = ""
        if (n == null) n = ""
        if (o == null) o = ""

        return OverviewItem(x, y, z, l, m, n, o)
    }
}