package com.hmn.testaicode.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateWalletResponse(
    val balance: Double,
    val currency: String,
    val phoneNumber: String,
    val status: String,
    val userId: String,
    val walletId: String
)