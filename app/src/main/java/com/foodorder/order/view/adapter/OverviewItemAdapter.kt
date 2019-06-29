package com.foodorder.order.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.foodorder.order.R
import com.foodorder.order.model.data.GlideRequests
import com.foodorder.order.view.componet.UnifiedImageView
import com.foodorder.order.view.componet.UnifiedTextView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

class OverviewItemAdapter(options: FirestoreRecyclerOptions<OverviewItem>, glide: GlideRequests) :
    FirestoreRecyclerAdapter<OverviewItem, OverviewItemAdapter.OverviewItemHolder>(options) {

    private var mItemClick: OnItemClickInterface? = null
    private val mGlide: GlideRequests = glide

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewItemHolder {
        return createItemHolder(parent, mItemClick)
    }

    override fun onBindViewHolder(holder: OverviewItemHolder, position: Int, item: OverviewItem) {
        holder.bindView(item, mGlide)
    }

    override fun getItem(position: Int): OverviewItem {
        return super.getItem(position)
    }

    override fun getItemCount(): Int {
        val x = super.getItemCount()
        return x
    }

    override fun getItemViewType(position: Int): Int {
        val x = super.getItemViewType(position)
        return x
    }

    //better to handle at ViewModel
    fun deleteItem(position: Int) {
        val it = snapshots.getSnapshot(position).reference
        it.delete()
    }

    fun getSnapshotRef(position: Int): DocumentReference {
        return snapshots.getSnapshot(position).reference
    }

    override fun onDataChanged() {
        super.onDataChanged()
    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)
    }

    interface OnItemClickInterface {
        fun onItemClick(snapshot: DocumentSnapshot, position: Int)
    }

    fun setOnItemClick(it: OnItemClickInterface) {
        mItemClick = it
    }

    private fun createItemHolder(parent: ViewGroup, clickInterface: OnItemClickInterface?): OverviewItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upload_overview_item, parent, false)
        return OverviewItemHolder(itemView, clickInterface)
    }

    inner class OverviewItemHolder(itemView: View, it: OnItemClickInterface?) :
        RecyclerView.ViewHolder(itemView) {

        val mItemImage = itemView.findViewById<UnifiedImageView>(R.id.upload_overview_item_image)
        val mItemId = itemView.findViewById<UnifiedTextView>(R.id.upload_overview_item_id)
        val mItemName = itemView.findViewById<UnifiedTextView>(R.id.upload_overview_item_name)
        private var mItemClickInterface: OnItemClickInterface? = null
        private var onClickListener = View.OnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                mItemClickInterface?.onItemClick(snapshots.getSnapshot(position), position)
            }
        }

        init {
            mItemClickInterface = it
            itemView.setOnClickListener(onClickListener)
        }

        fun bindView(it: OverviewItem, glide: GlideRequests) {
            mItemId.text = it.uniqueId
            mItemName.text = it.remoteInfo.name
            if (it.remoteImage.imageRemoteAddr != "") {
                glide.load(it.remoteImage.imageRemoteAddr).into(mItemImage)
            } else {
                glide.load(R.drawable.orange).into(mItemImage)
            }
        }
    }
}