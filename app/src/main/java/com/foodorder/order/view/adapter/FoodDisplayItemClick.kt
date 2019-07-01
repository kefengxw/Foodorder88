package com.foodorder.order.view.adapter

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.foodorder.order.R
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView
import com.foodorder.order.view.fragment.ItemDetailDialog

interface FoodDisplayItemClick {

    fun handleDisplayItemClick(itemView: View, activity: FragmentActivity) {

        val itemImage: UnifiedImageView = itemView.findViewById(R.id.item_image)
        val itemAdd: UnifiedImageView = itemView.findViewById(R.id.item_add)
        val itemRemove: UnifiedImageView = itemView.findViewById(R.id.item_remove)
        val itemTextNumber: UnifiedTextView = itemView.findViewById(R.id.item_text_number)

        itemImage.setOnClickListener(View.OnClickListener {
            ItemDetailDialog.startItemDetailDialog(activity.supportFragmentManager)
        })

        itemAdd.setOnClickListener(View.OnClickListener {
            itemTextNumber.text = (Integer.parseInt(itemTextNumber.text.toString()) + 1).toString()
        })

        itemRemove.setOnClickListener(View.OnClickListener {

            var x = (Integer.parseInt(itemTextNumber.text.toString()) - 1)//.toString()
            if (x < 0) x = 0
            itemTextNumber.text = x.toString()
        })
        //itemTextNumber.setOnClickListener(View.OnClickListener {})
    }
}