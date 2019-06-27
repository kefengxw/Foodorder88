package com.foodorder.order.model.repository

import com.foodorder.order.model.data.AppExecutors
import com.foodorder.order.model.data.InternalDataConfiguration.shouldTakeToken
import com.foodorder.order.model.data.InternalDataConfiguration.updateToken
import com.foodorder.order.model.data.Resource
import com.foodorder.order.model.remote.RemoteDataRepository
import com.foodorder.order.model.remote.RemoteToken
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Response

class DataRepository(val mEx: AppExecutors, val mRemoteDataRepository: RemoteDataRepository) {

    fun getRemoteToken(): Flowable<Resource<String>> {

        val nBResource = object : NetworkBoundResource<String, RemoteToken>(mEx) {

            override fun shouldGetRemoteToken(): Boolean {
                return shouldTakeToken()
            }

            override fun createTokenCall(): Single<Response<RemoteToken>> {
                return mRemoteDataRepository.getRemoteToken()
            }

            override fun convertTokenCallResult(data: RemoteToken) {
                updateToken(data.accessToken)
            }
        }

        return nBResource.asFlowable()
    }
}