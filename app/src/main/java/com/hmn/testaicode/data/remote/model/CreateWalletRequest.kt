package com.hmn.testaicode.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateWalletRequest(
    val  userId: String,
    val phoneNumber: String
)