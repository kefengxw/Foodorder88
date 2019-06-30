package com.foodorder.order.view.fragment

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.foodorder.order.R
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView
import com.foodorder.order.view.fragment.ItemDetailDialog.Companion.startItemDetailDialog

abstract class BaseFragmentWithItemClick : BaseFragment() {

    abstract override fun providedFragmentLayoutId(): Int

    protected fun handleItemOrderOverview(itemView: View, activity: FragmentActivity) {

        val itemImage: UnifiedImageView = itemView.findViewById(R.id.item_image)
        val itemAdd: UnifiedImageView = itemView.findViewById(R.id.item_add)
        val itemRemove: UnifiedImageView = itemView.findViewById(R.id.item_remove)
        val itemTextNumber: UnifiedTextView = itemView.findViewById(R.id.item_text_number)//NumberPicker

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
            itemTextNumber.text = (Integer.parseInt(itemTextNumber.text.toString()) - 1).toString()
        })
        itemTextNumber.setOnClickListener(View.OnClickListener {
            Toast.makeText(mBaseCtx, "hello mItem Text Number$it", Toast.LENGTH_SHORT).show()
        })
    }
}