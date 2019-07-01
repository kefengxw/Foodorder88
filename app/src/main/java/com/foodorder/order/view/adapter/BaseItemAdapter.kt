package com.foodorder.order.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.model.data.GlideRequests
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

abstract class BaseItemAdapter<T : BaseItemHolder>(
    options: FirestoreRecyclerOptions<OverviewFoodItem>,
    glide: GlideRequests
) : FirestoreRecyclerAdapter<OverviewFoodItem, T>(options) {

    private var mItemClick: OnItemClickInterface? = null
    private val mGlide: GlideRequests = glide

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        return createItemHolder(parent, mItemClick)
    }

    override fun onBindViewHolder(holder: T, position: Int, item: OverviewFoodItem) {
        holder.bindView(item, mGlide)
    }

    override fun getItem(position: Int): OverviewFoodItem {
        return super.getItem(position)
    }

    override fun getItemCount(): Int {
        val x = super.getItemCount()//just for debug
        return x
    }

    override fun getItemViewType(position: Int): Int {
        val x = super.getItemViewType(position)//just for debug
        return x
    }

    //better to handle at ViewModel
    fun deleteItem(position: Int) {
        val it = snapshots.getSnapshot(position).reference
        it.delete()
    }

    fun getSnapshotRef(position: Int): DocumentReference {
        return snapshots.getSnapshot(position).reference
    }

    fun getSnapshot(): ObservableSnapshotArray<OverviewFoodItem> {
        return snapshots
    }

    override fun onDataChanged() {
        super.onDataChanged()
    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)
    }

    interface OnItemClickInterface {
        fun onItemClick(itemView: View, snapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClick(it: OnItemClickInterface) {
        mItemClick = it
    }

    abstract fun getItemLayout(): Int
    abstract fun getItemHolder(
        itemView: View,
        click: BaseItemAdapter.OnItemClickInterface?,
        snapshots: ObservableSnapshotArray<OverviewFoodItem>
    ): T

    private fun createItemHolder(parent: ViewGroup, clickInterface: OnItemClickInterface?): T {

        val layout = getItemLayout()
        val itemView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return getItemHolder(itemView, clickInterface, getSnapshot())
    }
}