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

    suspend fun setHijauanButtonFilled(blockId: Int, value: Boolean) {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_HIJAUAN_IN_BLOCK_$blockId")
        sessionForFeedingDataStore.edit { preferences ->
            preferences[blockPreferenceKey] = value
        }
    }

    fun isHijauanButtonFilled(blockId: Int): Flow<Boolean> {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_HIJAUAN_IN_BLOCK_$blockId")
        return sessionForFeedingDataStore.data.map { preferences ->
            preferences[blockPreferenceKey] ?: true
        }
    }

    suspend fun setKimiaButtonFilled(blockId: Int, value: Boolean) {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_KIMIA_IN_BLOCK_$blockId")
        sessionForFeedingDataStore.edit { preferences ->
            preferences[blockPreferenceKey] = value
        }
    }

    fun isKimiaButtonFilled(blockId: Int): Flow<Boolean> {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_KIMIA_IN_BLOCK_$blockId")
        return sessionForFeedingDataStore.data.map { preferences ->
            preferences[blockPreferenceKey] ?: true
        }
    }

    suspend fun setVitaminButtonFilled(blockId: Int, value: Boolean) {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_VITAMIN_IN_BLOCK_$blockId")
        sessionForFeedingDataStore.edit { preferences ->
            preferences[blockPreferenceKey] = value
        }
    }

    fun isVitaminButtonFilled(blockId: Int): Flow<Boolean> {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_VITAMIN_IN_BLOCK_$blockId")
        return sessionForFeedingDataStore.data.map { preferences ->
            preferences[blockPreferenceKey] ?: true
        }
    }

    suspend fun setTambahanButtonFilled(blockId: Int, value: Boolean) {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_TAMBAHAN_IN_BLOCK_$blockId")
        sessionForFeedingDataStore.edit { preferences ->
            preferences[blockPreferenceKey] = value
        }
    }

    fun isTambahanButtonFilled(blockId: Int): Flow<Boolean> {
        val blockPreferenceKey = booleanPreferencesKey("STATE_BUTTON_TAMBAHAN_IN_BLOCK_$blockId")
        return sessionForFeedingDataStore.data.map { preferences ->
            preferences[blockPreferenceKey] ?: true
        }
    }

    // clear all
    suspend fun clearFeedingStates() {
        sessionForFeedingDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}