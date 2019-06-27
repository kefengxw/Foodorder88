package com.foodorder.order.model.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.repository.DisplayItem

class RemoteDataPageSourceFactory(val mEx: AppExecutors, val mService: RemoteDataInfoService, val mInput: String) :
    DataSource.Factory<String, DisplayItem>() {

    val mDataSource = MutableLiveData<RemoteDataPageSource>()

    @Override
    override fun create(): DataSource<String, DisplayItem> {
        val it = RemoteDataPageSource(mEx, mService, mInput)
        mDataSource.postValue(it)
        return it
    }
}