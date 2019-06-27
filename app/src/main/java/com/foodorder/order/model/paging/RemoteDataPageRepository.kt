package com.foodorder.order.model.paging

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.data.ResourceListing
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.repository.DisplayItem

class RemoteDataPageRepository(val mEx: AppExecutors, val mService: RemoteDataInfoService) {

    //it will crate many times(ResourceListing), for create once for per name-query
    fun getRemoteDataPageResult(input: String): ResourceListing<DisplayItem> {

        val sourceFactory = RemoteDataPageSourceFactory(mEx, mService, input)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()

        val pagedListData = LivePagedListBuilder(sourceFactory, pagedListConfig)
            //.setFetchExecutor()
            .build()

        return ResourceListing(
            mPagedList = pagedListData,
            mNetworkState = switchMap(sourceFactory.mDataSource) { it.getPagedListRemoteState() }
        )
    }
}