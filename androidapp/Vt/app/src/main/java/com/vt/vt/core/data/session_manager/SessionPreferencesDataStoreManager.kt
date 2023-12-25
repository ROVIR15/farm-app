package com.vt.vt.core.data.session_manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vt.vt.core.data.source.remote.auth.dto.user_session.UserSession
import com.vt.vt.utils.FILE_PREFERENCE_DATASTORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = FILE_PREFERENCE_DATASTORE)

class SessionPreferencesDataStoreManager(context: Context) {

    private val sessionDataStore = context.dataStore
    private val usernameKey = stringPreferencesKey("USERNAME")
    private val userToken = stringPreferencesKey("TOKEN_AUTH")

    //private val passwordKey = stringPreferencesKey("password")
    private val isLoginKey = booleanPreferencesKey("LOGIN_STATE")

    suspend fun getToken(): String? {
        val preference = sessionDataStore.data.first()
        return preference[userToken]
    }

    fun getLoginSession(): Flow<UserSession> {
        return sessionDataStore.data.map { preferences ->
            UserSession(
                preferences[usernameKey] ?: "",
                preferences[userToken] ?: "",
                preferences[isLoginKey] ?: false
            )
        }
    }

    //login
    suspend fun saveUserLoginState(userSession: UserSession) {
        sessionDataStore.edit { preferences ->
            preferences[usernameKey] = userSession.username
            preferences[userToken] = userSession.token
            preferences[isLoginKey] = true
        }
    }

    //logout
    suspend fun removeLoginState() {
        sessionDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}