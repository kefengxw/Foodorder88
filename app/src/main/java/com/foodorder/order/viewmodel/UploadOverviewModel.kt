package com.foodorder.order.viewmodel

import android.app.Application
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.model.firebase.FirebaseCloudDbRepository
import com.foodorder.order.model.firebase.FirebaseCloudDbRepositoryFactory
import com.foodorder.order.view.adapter.OverviewFoodItem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query

class UploadOverviewModel(app: Application) : BaseViewModel(app) {

    private lateinit var mFbCloudDbRepos: FirebaseCloudDbRepository

    override fun initInjector(component: HomeApplicationComponent){

    }

    override fun initViewModel(app: Application) {
        //mFbCloudDbRepos = mInstanceApp.mInstanceFbCloudDbRepos
    }

    fun getQueryOrderByKey(): Query {
        return mFbCloudDbRepos.getFoodQueryOrderByKey()
    }

    fun getQueryWhereEqualTo(it: String): Query {
        return mFbCloudDbRepos.getFoodQueryWhereEqualTo(it)
    }

    fun deleteFood(docRef: DocumentReference, item: OverviewFoodItem) {
        mFbCloudDbRepos.deleteFromFood(docRef, item)
    }

    override fun onCleared() {
        super.onCleared()
    }
}