package com.foodorder.order.view.adapter

import android.view.View
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView

class FoodUploadItemHolder(
    itemView: View,
    it: BaseItemAdapter.OnItemClickInterface?,
    snapshots: ObservableSnapshotArray<OverviewFoodItem>
) : BaseItemHolder(itemView, it, snapshots) {

    val mItemImage = baseItemFindViewById<UnifiedImageView>(R.id.upload_overview_item_image)
    val mItemId = baseItemFindViewById<UnifiedTextView>(R.id.upload_overview_item_id)
    val mItemName = baseItemFindViewById<UnifiedTextView>(R.id.upload_overview_item_name)

    override fun bindView(it: OverviewFoodItem, glide: GlideRequests) {
        //bind the View
        mItemId.text = it.uniqueId
        mItemName.text = it.remoteInfo.name
        if (it.remoteImage.imageRemoteAddr != "") {
            glide.load(it.remoteImage.imageRemoteAddr).into(mItemImage)
        } else {
            glide.load(R.drawable.orange).into(mItemImage)
        }
    }
}