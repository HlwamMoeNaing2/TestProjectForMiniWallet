package com.hmn.testaicode.data

import com.hmn.testaicode.data.remote.model.CreateUserModelRequest
import com.hmn.testaicode.data.remote.model.CreateUserModelResponse
import com.hmn.testaicode.data.remote.model.CreateWalletRequest
import com.hmn.testaicode.data.remote.model.CreateWalletResponse
import com.hmn.testaicode.data.remote.model.UserDetail

interface WalletUserRepo {
    suspend fun getUserDetail(phone: String): SimpleResponseHandler<UserDetail>

    suspend fun createUser(body: CreateUserModelRequest): SimpleResponseHandler<CreateUserModelResponse>

    suspend fun createWallet(body: CreateWalletRequest): SimpleResponseHandler<CreateWalletResponse>
}