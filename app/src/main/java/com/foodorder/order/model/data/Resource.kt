package com.foodorder.order.model.data

import com.foodorder.order.model.data.Status.*

class Resource<T>(val mStatus: Status, val mData: T?, val mMessage: String? = null) {

    companion object {
        fun <T> loadingData(data: T?): Resource<T> = Resource(LOADING, data)
        fun <T> successData(data: T): Resource<T> = Resource(SUCCESS, data)
        fun <T> errorData(data: T?, msg: String? = null): Resource<T> = Resource(ERROR, data, msg)
    }
}