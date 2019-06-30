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

data class FirebaseUserResult(
    val mStatus: Status,
    val mUser: GetUserDataUnitRemoteFb? = null,
    val mMsg: String? = null
) {
    companion object {
        fun loadingFbUserData() = FirebaseUserResult(Status.LOADING)
        fun successFbUserData(user: GetUserDataUnitRemoteFb?) = FirebaseUserResult(Status.SUCCESS, user)
        fun errorFbUserData(msg: String?) = FirebaseUserResult(Status.ERROR, mMsg = msg)
    }
}