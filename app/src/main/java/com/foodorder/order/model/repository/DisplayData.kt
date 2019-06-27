package com.foodorder.order.model.repository

data class DisplayData(
    var mTotal: Int = 0,
    var mPrev: String = "",
    var mNext: String = "",
    var mItem: List<DisplayItem>? = null
)

data class DisplayItem( //each artist information
    val mName: String = "",
    val mImagesUrl: String = ""
)