package com.foodorder.order.model.remote

import com.foodorder.order.model.data.ExternalDataConfiguration.CLIENT_ID
import com.foodorder.order.model.data.ExternalDataConfiguration.CLIENT_SECRET
import com.foodorder.order.model.data.ExternalDataConfiguration.GRANT_TYPE
import io.reactivex.Single
import retrofit2.Response

class RemoteDataRepository(remoteService: RemoteDataInfoService) {

    private var mDataInfoService: RemoteDataInfoService = remoteService

    //fun getRemoteToken(body: String = "client_credentials"): Single<Response<RemoteToken>> {
    fun getRemoteToken(): Single<Response<RemoteToken>> {
        return mDataInfoService.getRemoteToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET)
    }
}