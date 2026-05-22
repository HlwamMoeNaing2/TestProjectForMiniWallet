package com.hmn.testaicode.data

import android.util.Log
import com.hmn.testaicode.data.remote.api.WalletApiService
import com.hmn.testaicode.data.remote.model.UserDetail
import javax.inject.Inject

class WalletUserRepoImpl @Inject constructor (private val apiService: WalletApiService) :WalletUserRepo{
    override suspend fun getUserDetail(phone: String): SimpleResponseHandler<UserDetail> {
        return try {
            val value = apiService.getUser(phone)
            Log.d("#MMLog", "WalletUserRepoImpl: Try Block ")
            SimpleResponseHandler.Success(value)
        }catch (e: Exception){
            Log.d("#MMLog", "WalletUserRepoImpl: Catch Block ")
            SimpleResponseHandler.Error(e)
        }

    }
}