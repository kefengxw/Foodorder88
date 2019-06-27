package com.foodorder.order.model.remote

import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.data.ExternalDataConfiguration
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor

object RetrofitClient {

    @Volatile
    private var mInstanceRc: Retrofit? = null

    @Synchronized
    fun getInstanceRc(appEx: AppExecutors): Retrofit {
        if (mInstanceRc == null) {
            mInstanceRc = buildRetrofit(appEx)
        }
        return mInstanceRc!!
    }

    fun destroyInstanceRc() {
        mInstanceRc = null
    }

    //Might many uploadRemoteFood service to Expansion, check it later
    fun <T> createService(service: Class<T>, appEx: AppExecutors): T {
        return getInstanceRc(appEx).create(service)
    }

    private fun buildRetrofit(appEx: AppExecutors): Retrofit {

        val rbuilder = Retrofit.Builder()
        rbuilder.baseUrl(ExternalDataConfiguration.BASE_URL)
            .client(buildOkHttpClient())
            .callbackExecutor(buildRetrofitExecutor(appEx))
            .addCallAdapterFactory(buildCallAdapterFactoryRxJava())
            .addConverterFactory(buildConverterFactory())
        return rbuilder.build()
    }

    private fun buildOkHttpClient(): OkHttpClient {

        val obuilder = OkHttpClient.Builder()
        val it = buildLoggingInterceptor()
        obuilder.addInterceptor(it)
        return obuilder.build()
    }

    private fun buildConverterFactory(): Converter.Factory {

        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    private fun buildCallAdapterFactoryRxJava(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    private fun buildRetrofitExecutor(appEx: AppExecutors): Executor {
        return appEx.mNetworkIO
    }

    private fun buildLoggingInterceptor(): HttpLoggingInterceptor {

        val it = HttpLoggingInterceptor()
        it.level = HttpLoggingInterceptor.Level.BODY//NONE
        return it
    }
}