package com.hmn.testaicode.data

import kotlinx.coroutines.flow.Flow

interface AuthSessionRepository {
    val isLoggedIn: Flow<Boolean>

    suspend fun setLoggedIn(loggedIn: Boolean)

    val userPhone: Flow<String>
    suspend fun saveUserPhoner(ph: String)

}
