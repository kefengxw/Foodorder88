package com.foodorder.order.model.data

import com.foodorder.order.model.data.Status.*

enum class Phase {
    AUTH_LOGIN, FETCH_DATA
}

enum class Status {
    LOADING, SUCCESS, ERROR
}

data class NetworkState(val mStatus: Status, val mMsg: String? = null) {

    companion object {
        fun loadingNwData() = NetworkState(LOADING)
        fun successNwData() = NetworkState(SUCCESS)
        fun errorNwData(msg: String?) = NetworkState(ERROR, msg)
    }
}