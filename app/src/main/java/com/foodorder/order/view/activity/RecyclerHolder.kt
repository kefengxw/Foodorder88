package com.foodorder.order.view.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.model.repository.DisplayItem
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView

class RecyclerHolder(
    itemView: View,
    glide: GlideRequests,
    listener: RecyclerAdapter.OnItemClickListener?
) : RecyclerView.ViewHolder(itemView) {//can be static class if outside want to use it

    private var mItemLer: RecyclerAdapter.OnItemClickListener? = null
    private val mGlide = glide
    var mIvFlag: UnifiedImageView = itemView.findViewById(R.id.item_image)
    var mTvName: UnifiedTextView = itemView.findViewById(R.id.item_text)

    private val onClickListener = View.OnClickListener {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            mItemLer?.onItemClick(position)
        }
    }

    init {
        mItemLer = listener
        itemView.setOnClickListener(onClickListener)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: GlideRequests,
            listener: RecyclerAdapter.OnItemClickListener?
        ): RecyclerHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
            return RecyclerHolder(itemView, glide, listener)
        }
    }

    fun bindView(currentData: DisplayItem?) {
        currentData?.let {
            mTvName.text = currentData.mName
            //just test Coroutine
            //activity.mActivityCoroutineScope.launch
            {
                if (currentData.mImagesUrl == "") {
                    mGlide.load(R.drawable.default_image_icon)
                        .into(mIvFlag)//default Image
                } else {
                    mGlide.load(currentData.mImagesUrl)
                        //.placeholder(R.drawable.default_image_icon)
                        .into(mIvFlag)
                }
            }
        }
    }
}