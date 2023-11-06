package com.vt.vt.core.data.source.remote.dummy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore("my_preference")

class SessionFeedingDataStoreManager(context: Context) {
    private val sessionForFeedingDataStore: DataStore<Preferences> = context.createDataStore

    private val hijauanButton = booleanPreferencesKey("HIJAUAN_HAS_BEEN_FILLED")
    private val kimiaButton = booleanPreferencesKey("KIMIA_HAS_BEEN_FILLED")
    private val vitaminButton = booleanPreferencesKey("VITAMIN_HAS_BEEN_FILLED")
    private val tambahanButton = booleanPreferencesKey("TAMBAHAN_HAS_BEEN_FILLED")

    suspend fun setHijauanButtonFilled(value: Boolean) {
        sessionForFeedingDataStore.edit { preferences ->
            preferences[hijauanButton] = value
        }
    }

    fun isHijauanButtonFilled(): Flow<Boolean> {
        return sessionForFeedingDataStore.data.map {
            it[hijauanButton] ?: true
        }
    }

    suspend fun setKimiaButtonFilled(value: Boolean) {
        sessionForFeedingDataStore.edit { preferences ->
            preferences[kimiaButton] = value
        }
    }

    fun isKimiaButtonFilled(): Flow<Boolean> {
        return sessionForFeedingDataStore.data.map {
            it[kimiaButton] ?: true
        }
    }

    suspend fun setVitaminButtonFilled(value: Boolean) {
        sessionForFeedingDataStore.edit { preferences ->
            preferences[vitaminButton] = value
        }
    }

    fun isVitaminButtonFilled(): Flow<Boolean> {
        return sessionForFeedingDataStore.data.map {
            it[vitaminButton] ?: true
        }
    }

    suspend fun setTambahanButtonFilled(value: Boolean) {
        sessionForFeedingDataStore.edit { preferences ->
            preferences[tambahanButton] = value
        }
    }

    fun isTambahanButtonFilled(): Flow<Boolean> {
        return sessionForFeedingDataStore.data.map {
            it[tambahanButton] ?: true
        }
    }

    // clear all
    suspend fun clearFeedingStates() {
        sessionForFeedingDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}