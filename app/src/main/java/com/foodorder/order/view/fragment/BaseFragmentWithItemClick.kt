package com.foodorder.order.view.fragment

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.foodorder.order.R
import com.foodorder.order.view.activity.BaseRecyclerView
import com.foodorder.order.view.adapter.FoodDisplayItemAdapter
import com.foodorder.order.view.adapter.FoodDisplayItemHolder
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView
import com.foodorder.order.view.fragment.ItemDetailDialog.Companion.startItemDetailDialog

abstract class BaseFragmentWithItemClick : BaseFragment(),
    BaseRecyclerView<FoodDisplayItemHolder, FoodDisplayItemAdapter> {


    protected fun handleDisplayItemClick(itemView: View, activity: FragmentActivity) {

        val itemImage: UnifiedImageView = itemView.findViewById(R.id.item_image)
        val itemAdd: UnifiedImageView = itemView.findViewById(R.id.item_add)
        val itemRemove: UnifiedImageView = itemView.findViewById(R.id.item_remove)
        val itemTextNumber: UnifiedTextView = itemView.findViewById(R.id.item_text_number)

        itemImage.setOnClickListener(View.OnClickListener {
            //Toast.makeText(ctx, "hello mItem Image$it", Toast.LENGTH_SHORT).show()
            startItemDetailDialog(activity.supportFragmentManager)
        })
        itemAdd.setOnClickListener(View.OnClickListener {
            Toast.makeText(mBaseCtx, "hello mItem add$it", Toast.LENGTH_SHORT).show()
            itemTextNumber.text = (Integer.parseInt(itemTextNumber.text.toString()) + 1).toString()
        })
        itemRemove.setOnClickListener(View.OnClickListener {
            Toast.makeText(mBaseCtx, "hello mItem remove$it", Toast.LENGTH_SHORT).show()

            var x = (Integer.parseInt(itemTextNumber.text.toString()) - 1)//.toString()
            if (x < 0) x = 0
            itemTextNumber.text = x.toString()
        })
        itemTextNumber.setOnClickListener(View.OnClickListener {
            Toast.makeText(mBaseCtx, "hello mItem Text Number$it", Toast.LENGTH_SHORT).show()
        })
    }
}