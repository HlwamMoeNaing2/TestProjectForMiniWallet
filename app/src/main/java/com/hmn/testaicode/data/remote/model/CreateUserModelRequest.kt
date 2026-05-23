package com.hmn.testaicode.data.remote.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CreateUserModelRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val firstName: String = "",
    val lastName : String= "",
    val address: String ,
    val city: String,
    val  zipCode: String

)


@JsonClass(generateAdapter = true)
data class CreateUserModelResponse(
    val userId: String = "",
    val name: String,
    val email: String,
    val phoneNumber: String,
    val firstName: String = "",
    val lastName : String= "",
    val address: String ,
    val city: String,
    val  zipCode: String

)