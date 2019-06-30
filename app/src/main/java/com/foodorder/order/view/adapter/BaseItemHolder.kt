package com.foodorder.order.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.model.data.GlideRequests

abstract class BaseItemHolder(
    itemView: View,
    click: BaseItemAdapter.OnItemClickInterface?,
    snapshots: ObservableSnapshotArray<OverviewItem>
) : RecyclerView.ViewHolder(itemView) {

    private val mItemView = itemView
    private val mSnapshots = snapshots
    private var mItemClickInterface: BaseItemAdapter.OnItemClickInterface? = null
    private var onClickListener = View.OnClickListener {

        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            mItemClickInterface?.onItemClick(mSnapshots.getSnapshot(position), position)
        }
    }

    init {
        mItemClickInterface = click
        mItemView.setOnClickListener(onClickListener)
    }

    fun <T : View> baseItemFindViewById(id: Int): T {
        return mItemView.findViewById<T>(id)
    }

    abstract fun bindView(it: OverviewItem, glide: GlideRequests)
}