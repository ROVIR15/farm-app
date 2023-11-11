package com.vt.vt.core.data.source.remote.dummy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.Type

private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore("my_preference")

class SessionFeedingDataStoreManager(context: Context) {
    private val sessionForFeedingDataStore: DataStore<Preferences> = context.createDataStore

    private val mapKey = stringPreferencesKey("map_key")
    suspend fun saveMap(inputMap: Map<Int, MutableList<ConsumptionRecordItem>>) {
        val existingData = loadMap().first()
        val combinedData = existingData.toMutableMap()
        inputMap.forEach { (key, valueList) ->
            if (combinedData.containsKey(key)) {
                combinedData[key]?.addAll(valueList)
            } else {
                combinedData[key] = valueList.toMutableList()
            }
        }

        val mapString = Gson().toJson(combinedData)
        sessionForFeedingDataStore.edit { preferences ->
            preferences[mapKey] = mapString
        }
    }

    fun loadMap(): Flow<Map<Int, MutableList<ConsumptionRecordItem>>> {
        val mapType: Type =
            object : TypeToken<Map<Int, MutableList<ConsumptionRecordItem>>>() {}.type
        return sessionForFeedingDataStore.data.map { preferences ->
            val mapString = preferences[mapKey] ?: "{}"
            try {
                val map = Gson().fromJson<Map<Int, MutableList<ConsumptionRecordItem>>>(
                    mapString, mapType
                )
                map
            } catch (e: IOException) {
                e.printStackTrace()
                emptyMap()
            }
        }
    }

    suspend fun setHijauanButtonFilled(
        blockId: Int, value: Boolean
    ) {
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

    // clear by block
    suspend fun clearFeeding(blockId: Int) {
        val existingData = loadMap().first()
        val modifiedData = existingData.toMutableMap()
        modifiedData.remove(blockId)
        val mapString = Gson().toJson(modifiedData)
        sessionForFeedingDataStore.edit { preferences ->
            val buttonTypes =
                listOf("HIJAUAN", "KIMIA", "VITAMIN", "TAMBAHAN")
            buttonTypes.forEach { buttonType ->
                val blockPreferenceKey =
                    booleanPreferencesKey("STATE_BUTTON_${buttonType}_IN_BLOCK_${blockId}")
                preferences.remove(blockPreferenceKey)
            }
            preferences[mapKey] = mapString
        }
    }
}