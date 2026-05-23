package com.hmn.testaicode.domain

import com.hmn.testaicode.data.AuthSessionRepository
import com.hmn.testaicode.data.SimpleResponseHandler
import com.hmn.testaicode.data.WalletUserRepo
import com.hmn.testaicode.data.remote.model.CreateUserModelRequest
import com.hmn.testaicode.data.remote.model.CreateWalletRequest
import com.hmn.testaicode.data.remote.model.CreateWalletResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateUserAndWalletUc @Inject constructor(
    private val walletUserRepo:WalletUserRepo,
    private val authSessionRepository: AuthSessionRepository,
) {
    suspend fun createUser(body: CreateUserModelRequest):SimpleResponseHandler<String>{
        val phone = authSessionRepository.userPhone.first()
        val updateRequest  = body.copy(phoneNumber = phone)
        return when (val user = walletUserRepo.createUser(updateRequest)){
            is SimpleResponseHandler.Error -> SimpleResponseHandler.Error(user.exception)
            is SimpleResponseHandler.Success -> {

                val userId = user.data.userId
                val createWalletRequest = CreateWalletRequest(userId = userId , phoneNumber = phone)
                createWallet(createWalletRequest)
                 SimpleResponseHandler.Success("Sucess")
            }
        }
    }


    suspend fun createWallet(body:CreateWalletRequest): SimpleResponseHandler<CreateWalletResponse?>{
        return when(val wallet = walletUserRepo.createWallet(body)){
            is SimpleResponseHandler.Error ->SimpleResponseHandler.Error(Throwable("Something wrong"))
            is SimpleResponseHandler.Success -> SimpleResponseHandler.Success(wallet.data)
        }
    }
}