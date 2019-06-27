package com.foodorder.order.model.remote

object RemoteDataRepositoryFactory {

    @Volatile
    private var mInstanceReposService: RemoteDataRepository? = null

    @Synchronized
    fun getInstanceReposService(remoteService: RemoteDataInfoService): RemoteDataRepository {

        if (mInstanceReposService == null) {
            mInstanceReposService = RemoteDataRepository(remoteService)
        }
        return mInstanceReposService!!
    }

    fun destroyInstanceReposService() {
        mInstanceReposService = null
    }
}
