package com.vt.vt.core.data.local.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vt.vt.utils.FILE_PREFERENCE_DATASTORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = FILE_PREFERENCE_DATASTORE)

class SessionPreferencesDataStoreManager(context: Context) {

    private val sessionDataStore = context.dataStore
    private val isLoginKey = booleanPreferencesKey("LOGIN_STATE")

    fun getIsLoginState(): Flow<Boolean> {
        return sessionDataStore.data.map { preferences ->
            preferences[isLoginKey] ?: false
        }
    }

    //login
    suspend fun saveUserLoginState() {
        sessionDataStore.edit { preferences ->
            preferences[isLoginKey] = true
        }
    }

    //logout
    suspend fun removeLoginState() {
        sessionDataStore.edit { preferences ->
            preferences[isLoginKey] = false
        }
    }
}