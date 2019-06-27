package com.foodorder.order.model.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class ResourceListing<T>(
    val mNetworkState: LiveData<NetworkState>,
    val mPagedList: LiveData<PagedList<T>>
)