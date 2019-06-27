package com.foodorder.order.view.activity

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.model.repository.DisplayItem

class RecyclerAdapter(ctx: Context, glide: GlideRequests) :
    PagedListAdapter<DisplayItem, RecyclerHolder>(DataFilterDifferCallback) {
    private val mCtx = ctx//just for extension
    private val mGlide = glide
    private var mListener: OnItemClickListener? = null
    //private var mData: List<ItemRecyclerDisplayData> = ArrayList()//null;

//    fun setData(data: List<ItemRecyclerDisplayData>) {
//        this.mData = data
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder.create(parent, mGlide, mListener)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, iPosition: Int) {
        holder.bindView(getItem(iPosition))
    }

    override fun getItemCount(): Int {
        val it = super.getItemCount()
        return it
    }

    override fun getItem(position: Int): DisplayItem? {
        return super.getItem(position)
    }

    override fun getCurrentList(): PagedList<DisplayItem>? {
        return super.getCurrentList()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

//    fun getPositionByIndex(it: String): Int {
//        if (mData.isEmpty() || 'A' > it[0] || it[0] > 'Z') {
//            return RecyclerView.NO_POSITION
//        }
//
//        for (i in mData.indices) {
//            if (UtilString.getIndex(mData[i].mName.toUpperCase()) == it) {
//                return i
//            }
//        }
//
//        return RecyclerView.NO_POSITION
//    }

    companion object {
        val DataFilterDifferCallback = object : DiffUtil.ItemCallback<DisplayItem>() {

            override fun areItemsTheSame(oldItem: DisplayItem, newItem: DisplayItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: DisplayItem, newItem: DisplayItem): Boolean =
                //if areItemsTheSame is true, than check this Contents
                oldItem.mName == newItem.mName
        }
    }
}