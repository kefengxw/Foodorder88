package com.foodorder.order.view.adapter

import android.view.View
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.componet.UnifiedButton
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView

class FoodDisplayItemHolder(
    itemView: View,
    it: BaseItemAdapter.OnItemClickInterface?,
    snapshots: ObservableSnapshotArray<OverviewFoodItem>
) : BaseItemHolder(itemView, it, snapshots) {

    val mItemImage = baseItemFindViewById<UnifiedImageView>(R.id.item_image)
//    val mItemAddBtn = baseItemFindViewById<UnifiedButton>(R.id.item_add)
//    val mItemRemoveBtn = baseItemFindViewById<UnifiedButton>(R.id.item_remove)
//    val mItemTextNum = baseItemFindViewById<UnifiedTextView>(R.id.item_text_number)

    override fun bindView(it: OverviewFoodItem, glide: GlideRequests) {
        //bind the View
        if (it.remoteImage.imageRemoteAddr != "") {
            glide.load(it.remoteImage.imageRemoteAddr).into(mItemImage)
        } else {
            glide.load(R.drawable.orange).into(mItemImage)
        }
    }
}