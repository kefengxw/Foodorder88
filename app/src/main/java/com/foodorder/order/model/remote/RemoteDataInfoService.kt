package com.foodorder.order.model.remote

import com.foodorder.order.model.data.InternalDataConfiguration.AUTH_TOKEN
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface RemoteDataInfoService {

    @Headers(
        "Content-type: application/x-www-form-urlencoded"
    )
    @FormUrlEncoded
    @POST(value = "https://accounts.spotify.com/api/token")//different BASE_URL
    fun getRemoteToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Single<Response<RemoteToken>>

    //Search for an Item, Endpoint	https://api.spotify.com/v1/search
    //@GET("search?q={name}&type={type}&market={market}&limit={limit}&offset={offset}")
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("search")
    fun getRemoteInfo(
        @Header("Authorization") auth: String = AUTH_TOKEN,
        @Query("q") name: String,
        @Query("type") type: String,
        @Query("market") market: String,
        @Query("limit") limit: Int,         //per page
        @Query("offset") offset: Int        //start position(offset-1)
    ): Single<Response<RemoteBean>>
}