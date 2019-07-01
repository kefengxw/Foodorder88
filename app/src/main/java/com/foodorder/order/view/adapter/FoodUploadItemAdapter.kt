package com.foodorder.order.view.adapter

import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests

class FoodUploadItemAdapter(
    options: FirestoreRecyclerOptions<OverviewFoodItem>,
    glide: GlideRequests
) : BaseItemAdapter<FoodUploadItemHolder>(options, glide) {

    override fun getItemLayout(): Int {
        return R.layout.upload_overview_item
    }

    override fun getItemHolder(
        itemView: View,
        click: OnItemClickInterface?,
        snapshots: ObservableSnapshotArray<OverviewFoodItem>
    ): FoodUploadItemHolder {
        return FoodUploadItemHolder(itemView, click, snapshots)
    }
}