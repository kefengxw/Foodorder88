package com.foodorder.order.model.firebase

object FirebaseAuthRepositoryFactory {

    @Volatile
    private var mInstanceFbAuthRepos: FirebaseAuthRepository? = null

    @Synchronized
    fun getInstanceFbAuthRepos(/*appEx: AppExecutors*/): FirebaseAuthRepository {

        if (mInstanceFbAuthRepos == null) {
            mInstanceFbAuthRepos = FirebaseAuthRepository()
        }
        return mInstanceFbAuthRepos!!
    }

    fun destroyInstanceFbAuthRepos() {
        mInstanceFbAuthRepos = null
    }
}