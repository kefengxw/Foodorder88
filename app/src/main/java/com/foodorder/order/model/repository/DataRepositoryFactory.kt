package com.foodorder.order.model.repository

import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.remote.RemoteDataRepository

object DataRepositoryFactory {

    @Volatile
    private var mInstanceRepos: DataRepository? = null

    @Synchronized
    fun getInstanceRepos(appEx: AppExecutors, remoteRepos: RemoteDataRepository): DataRepository {

        if (mInstanceRepos == null) {
            mInstanceRepos = DataRepository(appEx, remoteRepos)
        }
        return mInstanceRepos!!
    }

    fun destroyInstanceRepos() {
        mInstanceRepos = null
    }
}