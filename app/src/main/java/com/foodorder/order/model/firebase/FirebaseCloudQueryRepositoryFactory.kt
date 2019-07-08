package com.foodorder.order.model.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object FirebaseCloudQueryRepositoryFactory {

    @Volatile
    private var mInstanceFbCloudQueryRepos: FirebaseCloudQueryRepository? = null

    @Synchronized
    fun getInstanceFbCloudQueryRepos(
        fbCloudDatabase: FirebaseFirestore,
        fbCloudStorage: FirebaseStorage
    ): FirebaseCloudQueryRepository {

        if (mInstanceFbCloudQueryRepos == null) {
            mInstanceFbCloudQueryRepos = FirebaseCloudQueryRepository(fbCloudDatabase, fbCloudStorage)
        }
        return mInstanceFbCloudQueryRepos!!
    }

    fun destroyInstanceFbCloudQueryRepos() {
        mInstanceFbCloudQueryRepos = null
    }
}