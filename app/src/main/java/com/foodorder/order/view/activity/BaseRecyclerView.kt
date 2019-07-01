package com.foodorder.order.view.activity

//import com.foodorder.order.view.adapter.OverviewItemAdapter
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.adapter.BaseItemAdapter
import com.foodorder.order.view.adapter.BaseItemHolder
import com.foodorder.order.view.adapter.OverviewFoodItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

interface BaseRecyclerView<HolderT : BaseItemHolder, AdapterT : BaseItemAdapter<HolderT>> {

    //can be improved more, use <T> to replace OverviewFoodItem
    var mRecyclerView: RecyclerView
    var mAdapter: AdapterT//OverviewItemAdapter

    fun initRecyclerView(): RecyclerView
    fun initRecyclerViewAdapter(): AdapterT//OverviewItemAdapter
    fun createRecyclerViewAdapter(options: FirestoreRecyclerOptions<OverviewFoodItem>, glide: GlideRequests): AdapterT
    fun getGlide(): GlideRequests
    fun getLayoutManager(): LinearLayoutManager
    fun getQuery(): Query
    fun doActionOnRecyclerViewItemClick(itemView: View, actionData: OverviewFoodItem, position: Int)
    fun doActionOnRecyclerViewItemSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)

    fun initializeRecyclerView(enableSwipe: Boolean = false) {
        initLocalProcess(enableSwipe)
    }

    fun startRecyclerViewListening() {
        mAdapter.startListening()
    }

    fun stopRecyclerViewListening() {
        mAdapter.stopListening()
    }

    private fun initLocalProcess(enableSwipe: Boolean) {

        //val itemClick = object : OverviewItemAdapter.OnItemClickInterface {
        val itemClick = object : BaseItemAdapter.OnItemClickInterface {
            override fun onItemClick(itemView: View, snapshot: DocumentSnapshot, position: Int) {
                val it = getItemDataUnit(snapshot)
                doActionOnRecyclerViewItemClick(itemView, it, position)
            }
        }

        mAdapter.setOnItemClick(itemClick)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = getLayoutManager()
        mRecyclerView.adapter = mAdapter

        if (enableSwipe) {
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

            object : ItemTouchHelper(itemSwipe) {}.attachToRecyclerView(mRecyclerView)
        }
    }

    private fun getFirebaseOptions(convert: SnapshotParser<OverviewFoodItem>): FirestoreRecyclerOptions<OverviewFoodItem> {

        val query = getQuery()
        val options = FirestoreRecyclerOptions.Builder<OverviewFoodItem>()
            .setQuery(query, convert)
            .build()

        return options
    }

    fun getRecyclerViewAdapter(): AdapterT/*OverviewItemAdapter*/ {

        val convert = object : SnapshotParser<OverviewFoodItem> {
            override fun parseSnapshot(snapshot: DocumentSnapshot): OverviewFoodItem {
                return getItemDataUnit(snapshot)
            }
        }
        val glide = getGlide()
        val options = getFirebaseOptions(convert)

        return createRecyclerViewAdapter(options, glide)//OverviewItemAdapter(options, glide)
    }

    fun getItemDataUnit(snapshot: DocumentSnapshot): OverviewFoodItem {
        //snapshot.reference
        //也可以放到子类中进行处理，暂时放在这里，因为OverviewItem是一个统一的，如果不同的话，就需要考虑不同子类分开
        //var x = snapshot.getString("uniqueId")
        if (!snapshot.exists()) {
            //debug error here
        }

        val item: OverviewFoodItem = snapshot.toObject(OverviewFoodItem::class.java)!!
        return item
    }
}