package com.foodorder.order.model.firebase

object FirebaseCloudQueryRepositoryFactory {

    @Volatile
    private var mInstanceFbCloudQueryRepos: FirebaseCloudQueryRepository? = null

    @Synchronized
    fun getInstanceFbCloudQueryRepos(/*appEx: AppExecutors*/): FirebaseCloudQueryRepository {

        if (mInstanceFbCloudQueryRepos == null) {
            mInstanceFbCloudQueryRepos = FirebaseCloudQueryRepository()
        }
        return mInstanceFbCloudQueryRepos!!
    }

    fun destroyInstanceFbCloudQueryRepos() {
        mInstanceFbCloudQueryRepos = null
    }
}