package com.foodorder.order.di.module

import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.paging.RemoteDataPageRepository
import com.foodorder.order.model.paging.RemoteDataPageRepositoryFactory
import com.foodorder.order.model.remote.RemoteDataInfoService
import com.foodorder.order.model.remote.RemoteDataInfoServiceFactory
import com.foodorder.order.model.remote.RemoteDataRepository
import com.foodorder.order.model.remote.RemoteDataRepositoryFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteModule {

    @Singleton
    @Provides
    fun provideRemoteDataInfoService(appEx: AppExecutors): RemoteDataInfoService {
        return RemoteDataInfoServiceFactory.getInstanceService(appEx)
    }

    @Singleton
    @Provides
    fun provideRemoteDataRepository(remoteService: RemoteDataInfoService): RemoteDataRepository {
        return RemoteDataRepositoryFactory.getInstanceReposService(remoteService)
    }

    @Singleton
    @Provides
    fun provideRemoteDataPageRepository(
        appEx: AppExecutors,
        remoteService: RemoteDataInfoService
    ): RemoteDataPageRepository {
        return RemoteDataPageRepositoryFactory.getInstancePageRepos(appEx, remoteService)
    }
}