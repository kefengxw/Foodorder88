package com.foodorder.order.app

import android.content.SharedPreferences
import android.content.res.Configuration
import com.foodorder.order.di.component.DaggerHomeApplicationComponent
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.di.module.HomeApplicationModule
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.firebase.FirebaseAuthRepository
import com.foodorder.order.model.firebase.FirebaseCloudDbRepository
import com.foodorder.order.model.firebase.FirebaseCloudQueryRepository
import com.foodorder.order.model.paging.RemoteDataPageRepository
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.remote.RemoteDataRepository
import com.foodorder.order.model.repository.DataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject


class HomeApplication : BaseApplication() {

    companion object {
        lateinit var mHomeApp: HomeApplication//后续删除
    }

    lateinit var mInstanceApp: HomeApplication
    lateinit var mInstanceEx: AppExecutors
    lateinit var mInstanceService: RemoteDataInfoService
    lateinit var mInstanceReposService: RemoteDataRepository
    lateinit var mInstancePageRepos: RemoteDataPageRepository
    lateinit var mInstanceRepos: DataRepository
    lateinit var mInstanceFbAuth: FirebaseAuth
    lateinit var mInstanceFbFirestore: FirebaseFirestore
    lateinit var mInstanceFbStorage: FirebaseStorage
    lateinit var mInstanceFbAuthRepos: FirebaseAuthRepository
    //lateinit var mInstanceFbCloudDbRepos: FirebaseCloudDbRepository
    //lateinit var mInstanceFbCloudQueryRepos: FirebaseCloudQueryRepository

    lateinit var mInstanceSharedPreferences: SharedPreferences

    lateinit var mApplicationComponent: HomeApplicationComponent
    var currentCountry = ""

    override fun onCreate() {
        super.onCreate()
        init()
        //this.registerActivityLifecycleCallbacks();
    }

    private fun init() {
        initInjector()//Inject
        initCurrentCountry()
    }

    private fun initInjector() {
        mApplicationComponent = DaggerHomeApplicationComponent.builder()
            .homeApplicationModule(HomeApplicationModule(this))
            .build()
        mApplicationComponent.inject(this)
    }

    fun getApplicationComponent(): HomeApplicationComponent {
        return mApplicationComponent
    }

    @Inject
    fun setInstanceApp(instance: HomeApplication) {
        this.mInstanceApp = instance
        mHomeApp = instance//后续删除
    }

    @Inject
    fun setAppExecutors(appEx: AppExecutors) {
        mInstanceEx = appEx
    }

    @Inject
    fun setInstanceService(instance: RemoteDataInfoService) {
        mInstanceService = instance
    }

    @Inject
    fun setInstanceReposService(instance: RemoteDataRepository) {
        mInstanceReposService = instance
    }

    @Inject
    fun setInstanceReposDataSource(instance: RemoteDataPageRepository) {
        mInstancePageRepos = instance
    }

    @Inject
    fun setInstanceRepos(instance: DataRepository) {
        mInstanceRepos = instance
    }

    @Inject
    fun setInstanceFb(instance: FirebaseAuth) {
        mInstanceFbAuth = instance
    }

    @Inject
    fun setInstanceFbFirestore(instance: FirebaseFirestore) {
        mInstanceFbFirestore = instance
    }

    @Inject
    fun setInstanceFbStorage(instance: FirebaseStorage) {
        mInstanceFbStorage = instance
    }

    @Inject
    fun setInstanceFbAuthRepos(instance: FirebaseAuthRepository) {
        mInstanceFbAuthRepos = instance
    }

    //加入Timber.plant(new Timber.DebugTree());

    fun updateData(){

    }
    /*
    @Inject
    fun setInstanceFbCloudDbRepos(instance: FirebaseCloudDbRepository) {
        mInstanceFbCloudDbRepos = instance
    }

    @Inject
    fun setInstanceFbCloudQueryRepos(instance: FirebaseCloudQueryRepository) {
        mInstanceFbCloudQueryRepos = instance
    }
    */

    @Inject
    fun setInstanceSSharedPre(instance: SharedPreferences) {
        mInstanceSharedPreferences = instance
    }


    private fun initCurrentCountry() {
        currentCountry = resources.configuration.locales.get(0).country
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        newConfig?.let {
            currentCountry = newConfig.locales.get(0).country
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //log here, in case too much info
    }
}