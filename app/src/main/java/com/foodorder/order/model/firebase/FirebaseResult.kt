package com.foodorder.order.model.firebase

import com.foodorder.order.model.data.Status

data class FirebaseResult(
    val mStatus: Status,
    val mMsg: String? = null
) {
    companion object {
        fun loadingFbData() = FirebaseResult(Status.LOADING)
        fun successFbData() = FirebaseResult(Status.SUCCESS)
        fun errorFbData(msg: String?) = FirebaseResult(Status.ERROR, msg)
    }
}