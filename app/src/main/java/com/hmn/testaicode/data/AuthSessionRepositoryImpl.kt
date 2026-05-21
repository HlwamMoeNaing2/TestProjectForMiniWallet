package com.hmn.testaicode.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.hmn.testaicode.data.local.AuthSessionPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthSessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AuthSessionRepository {

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AuthSessionPreferencesKeys.IS_LOGGED_IN] ?: false
    }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[AuthSessionPreferencesKeys.IS_LOGGED_IN] = loggedIn
        }
    }
}
