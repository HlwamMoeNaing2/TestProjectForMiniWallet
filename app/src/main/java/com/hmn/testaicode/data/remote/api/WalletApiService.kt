package com.hmn.testaicode.data.remote.api

import com.hmn.testaicode.data.remote.model.UserDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface WalletApiService {
    @GET("user/{phoneNumber}")
    suspend fun getUser(
        @Path("phoneNumber") phoneNumber: String
    ): UserDetail

}