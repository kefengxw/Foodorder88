package com.foodorder.order.model.paging

import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.remote.RemoteDataInfoService

object RemoteDataPageRepositoryFactory {

    @Volatile
    private var mInstancePageRepos: RemoteDataPageRepository? = null

    @Synchronized
    fun getInstancePageRepos(ex: AppExecutors, remoteService: RemoteDataInfoService): RemoteDataPageRepository {

        if (mInstancePageRepos == null) {
            mInstancePageRepos = RemoteDataPageRepository(ex, remoteService)
        }
        return mInstancePageRepos!!
    }

    fun destroyInstancePageRepos() {
        mInstancePageRepos = null
    }
}