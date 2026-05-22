package com.hmn.testaicode.data

import com.hmn.testaicode.data.remote.model.User

interface UserRepository {
    suspend fun getUsers(): SimpleResponseHandler<List<User>>
    suspend fun getUser(id: Int): SimpleResponseHandler<User>
}
