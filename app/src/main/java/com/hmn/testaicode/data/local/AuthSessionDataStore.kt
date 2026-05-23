package com.hmn.testaicode.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object AuthSessionPreferencesKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    val PHONE_NO = stringPreferencesKey("user_phone")
}

val Context.authSessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_session_prefs",
)
