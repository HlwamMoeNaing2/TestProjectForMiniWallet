package com.hmn.testaicode.data

import com.hmn.testaicode.data.remote.api.JsonPlaceholderApi
import com.hmn.testaicode.data.remote.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: JsonPlaceholderApi,
) : UserRepository {

    override suspend fun getUsers(): SimpleResponseHandler<List<User>> {
        return try {
            SimpleResponseHandler.Success(api.getUsers())
        } catch (e: Exception) {
            SimpleResponseHandler.Error(e)
        }
    }

    override suspend fun getUser(id: Int): SimpleResponseHandler<User> {
        return try {
            SimpleResponseHandler.Success(api.getUser(id))
        } catch (e: Exception) {
            SimpleResponseHandler.Error(e)
        }
    }
}
