package com.foodorder.order.view.componet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.foodorder.order.R

class UnifiedSpinnerAdapter<T>(ctx: Context, list: ArrayList<T>) :
    ArrayAdapter<T>(ctx, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initSpinnerView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initSpinnerView(position, convertView, parent)
    }

    private fun initSpinnerView(position: Int, it: View?, parent: ViewGroup): View {

        //android.R.layout.simple_spinner_dropdown_item

        var convertView = it
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.unified_spinner, parent, false)
        }

        val textView = convertView!!.findViewById<UnifiedTextView>(R.id.unified_spinner_item_name)
        val currentItem = getItem(position) as UnifiedSpinnerItem

        if (currentItem != null) {
            textView.text = currentItem.mSpinnerItem
        }

        return convertView
    }
}