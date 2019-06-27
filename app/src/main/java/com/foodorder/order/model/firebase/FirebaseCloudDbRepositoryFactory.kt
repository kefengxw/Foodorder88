package com.foodorder.order.model.firebase

object FirebaseCloudDbRepositoryFactory {

    @Volatile
    private var mInstanceFbCloudDbRepos: FirebaseCloudDbRepository? = null

    @Synchronized
    fun getInstanceFbCloudDbRepos(/*appEx: AppExecutors*/): FirebaseCloudDbRepository {

        if (mInstanceFbCloudDbRepos == null) {
            mInstanceFbCloudDbRepos = FirebaseCloudDbRepository()
        }
        return mInstanceFbCloudDbRepos!!
    }

    fun destroyInstanceFbCloudDbRepos() {
        mInstanceFbCloudDbRepos = null
    }
}