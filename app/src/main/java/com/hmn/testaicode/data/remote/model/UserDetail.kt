package com.hmn.testaicode.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDetail(
    val address: Any,
    val city: Any,
    val email: String,
    val firstName: Any,
    val lastName: Any,
    val name: String,
    val phoneNumber: String,
    val userId: String,
    val zipCode: Any
)