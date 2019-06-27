package com.foodorder.order.model.remote

import com.foodorder.order.model.data.AppExecutors

object RemoteDataInfoServiceFactory {

    @Volatile
    private var mInstanceService: RemoteDataInfoService? = null

    @Synchronized
    fun getInstanceService(appEx: AppExecutors): RemoteDataInfoService {

        if (mInstanceService == null) {
            mInstanceService = RetrofitClient.createService(RemoteDataInfoService::class.java, appEx)
        }
        return mInstanceService!!
    }

    fun destroyInstanceService() {
        mInstanceService = null
    }
}