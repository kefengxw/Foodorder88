package com.foodorder.order.app

import android.content.res.Configuration
import com.foodorder.order.di.component.DaggerHomeApplicationComponent
import com.foodorder.order.di.component.HomeApplicationComponent
import com.foodorder.order.di.module.HomeApplicationModule
import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.firebase.FirebaseAuthRepository
import com.foodorder.order.model.paging.RemoteDataPageRepository
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.remote.RemoteDataRepository
import com.foodorder.order.model.repository.DataRepository
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
    lateinit var mApplicationComponent: HomeApplicationComponent
    var currentCountry = ""

    lateinit var mInstanceFbAuthRepos: FirebaseAuthRepository

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
    fun setInstanceApp(instanceApp: HomeApplication) {
        this.mInstanceApp = instanceApp
        mHomeApp = instanceApp//后续删除
    }

    @Inject
    fun setAppExecutors(appEx: AppExecutors) {
        mInstanceEx = appEx
    }

    @Inject
    fun setInstanceService(remoteService: RemoteDataInfoService) {
        mInstanceService = remoteService
    }

    @Inject
    fun setInstanceReposService(remoteRepos: RemoteDataRepository) {
        mInstanceReposService = remoteRepos
    }

    @Inject
    fun setInstanceReposDataSource(remotePageRepos: RemoteDataPageRepository) {
        mInstancePageRepos = remotePageRepos
    }

    @Inject
    fun setInstanceRepos(repos: DataRepository) {
        mInstanceRepos = repos
    }

    @Inject
    fun setInstanceFbAuthRepos(repos: FirebaseAuthRepository) {
        mInstanceFbAuthRepos = repos
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