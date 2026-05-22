package com.hmn.testaicode.data

import com.hmn.testaicode.data.remote.model.UserDetail

interface WalletUserRepo {
    suspend fun getUserDetail(phone: String): SimpleResponseHandler<UserDetail>
}