package com.foodorder.order.view.adapter

import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests

class FoodDisplayItemAdapter(
    options: FirestoreRecyclerOptions<OverviewFoodItem>,
    glide: GlideRequests
) : BaseItemAdapter<FoodDisplayItemHolder>(options, glide) {

    override fun getItemLayout(): Int {
        return R.layout.order_item_overview_main
    }

    override fun getItemHolder(
        itemView: View,
        click: OnItemClickInterface?,
        snapshots: ObservableSnapshotArray<OverviewFoodItem>
    ): FoodDisplayItemHolder {
        return FoodDisplayItemHolder(itemView, click, snapshots)
    }
}