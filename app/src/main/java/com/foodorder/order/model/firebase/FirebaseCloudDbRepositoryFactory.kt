package com.foodorder.order.model.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseCloudDbRepositoryFactory {

    @Volatile
    private var mInstanceFbCloudDbRepos: FirebaseCloudDbRepository? = null

    @Synchronized
    fun getInstanceFbCloudDbRepos(
        fbCloudDatabase: FirebaseFirestore,
        fbCloudStorage: FirebaseStorage
    ): FirebaseCloudDbRepository {

        if (mInstanceFbCloudDbRepos == null) {
            mInstanceFbCloudDbRepos = FirebaseCloudDbRepository(fbCloudDatabase, fbCloudStorage)
        }
        return mInstanceFbCloudDbRepos!!
    }

    fun destroyInstanceFbCloudDbRepos() {
        mInstanceFbCloudDbRepos = null
    }
}