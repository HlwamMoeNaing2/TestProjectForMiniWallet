package com.hmn.testaicode.data

import android.util.Log
import com.hmn.testaicode.data.remote.api.WalletApiService
import com.hmn.testaicode.data.remote.model.CreateUserModelRequest
import com.hmn.testaicode.data.remote.model.CreateUserModelResponse
import com.hmn.testaicode.data.remote.model.CreateWalletRequest
import com.hmn.testaicode.data.remote.model.CreateWalletResponse
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

    override suspend fun createUser(body: CreateUserModelRequest): SimpleResponseHandler<CreateUserModelResponse> {
        return try {

            val createUserResponse = apiService.createUser(body)

            if (createUserResponse.isSuccessful){
                val result = createUserResponse.body()
                if (result == null){
                    SimpleResponseHandler.Error(Throwable("Null"))
                }else{
                    SimpleResponseHandler.Success(result)
                }
            }else{
                val statusCode = createUserResponse.code()

                if (statusCode == 400 ){
                    SimpleResponseHandler.Error(Throwable("400 Returning"))
                }else{
                    SimpleResponseHandler.Error(Throwable("Null"))
                }

            }
        }catch (e: Exception){
            SimpleResponseHandler.Error(e)
        }
    }

    override suspend fun createWallet(body: CreateWalletRequest): SimpleResponseHandler<CreateWalletResponse> {
        return try {

            val createUserResponse = apiService.createWallet(body)

            if (createUserResponse.isSuccessful){
                val result = createUserResponse.body()
                if (result == null){
                    SimpleResponseHandler.Error(Throwable("Null"))
                }else{
                    SimpleResponseHandler.Success(result)
                }
            }else{
                val statusCode = createUserResponse.code()

                if (statusCode == 400 ){
                    SimpleResponseHandler.Error(Throwable("400 Returning"))
                }else{
                    SimpleResponseHandler.Error(Throwable("Null"))
                }

            }
        }catch (e: Exception){
            SimpleResponseHandler.Error(e)
        }
    }
}