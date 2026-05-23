package com.hmn.testaicode.data.remote.api

import com.hmn.testaicode.data.remote.model.CreateUserModelRequest
import com.hmn.testaicode.data.remote.model.CreateUserModelResponse
import com.hmn.testaicode.data.remote.model.CreateWalletRequest
import com.hmn.testaicode.data.remote.model.CreateWalletResponse
import com.hmn.testaicode.data.remote.model.UserDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WalletApiService {
    @GET("user/{phoneNumber}")
    suspend fun getUser(
        @Path("phoneNumber") phoneNumber: String
    ): UserDetail



    @POST("/api/v1/user")
    suspend fun createUser(
        @Body body: CreateUserModelRequest
    ): Response<CreateUserModelResponse>

    @POST("/api/wallet")
    suspend fun createWallet(
        @Body body: CreateWalletRequest
    ): Response<CreateWalletResponse>


}