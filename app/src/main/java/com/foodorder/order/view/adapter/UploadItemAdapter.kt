package com.foodorder.order.view.adapter

import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests

class UploadItemAdapter(
    options: FirestoreRecyclerOptions<OverviewItem>,
    glide: GlideRequests
) : BaseItemAdapter<UploadItemHolder>(options, glide) {

    override fun getItemLayout(): Int {
        return R.layout.upload_overview_item
    }

    override fun getItemHolder(
        itemView: View,
        click: OnItemClickInterface?,
        snapshots: ObservableSnapshotArray<OverviewItem>
    ): UploadItemHolder {
        return UploadItemHolder(itemView, click, snapshots)
    }
}