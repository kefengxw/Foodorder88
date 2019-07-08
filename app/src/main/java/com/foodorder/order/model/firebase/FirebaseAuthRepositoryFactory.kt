package com.foodorder.order.model.firebase

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthRepositoryFactory {

    @Volatile
    private var mInstanceFbAuthRepos: FirebaseAuthRepository? = null

    @Synchronized
    fun getInstanceFbAuthRepos(fbAuth: FirebaseAuth): FirebaseAuthRepository {

        if (mInstanceFbAuthRepos == null) {
            mInstanceFbAuthRepos = FirebaseAuthRepository(fbAuth)
        }
        return mInstanceFbAuthRepos!!
    }

    fun destroyInstanceFbAuthRepos() {
        mInstanceFbAuthRepos = null
    }
}