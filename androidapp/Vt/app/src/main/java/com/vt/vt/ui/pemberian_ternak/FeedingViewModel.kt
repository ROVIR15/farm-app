package com.vt.vt.ui.pemberian_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.session_manager.SessionFeedingDataStoreManager
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import com.vt.vt.core.data.source.repository.FeedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class FeedingViewModel @Inject constructor(
    private val sessionFeedingDataStoreManager: SessionFeedingDataStoreManager,
    private val feedingVtRepository: FeedingVtRepository
) : BaseViewModel() {

    private val _feedingEmitter = MutableLiveData<FeedingRecordResponse>()
    val feedingEmitter: LiveData<FeedingRecordResponse> = _feedingEmitter

    private val _pushFeeding =
        MutableLiveData<Pair<Boolean, Map<Int, MutableList<ConsumptionRecordItem>>>>()
    val pushFeeding: LiveData<Pair<Boolean, Map<Int, MutableList<ConsumptionRecordItem>>>> =
        _pushFeeding

    fun push(map: Map<Int, MutableList<ConsumptionRecordItem>>) {
        viewModelScope.launch {
            var isCommitSuccessful = false
            try {
                sessionFeedingDataStoreManager.saveMap(map)
                isCommitSuccessful = true
            } catch (e: Exception) {
                isException.postValue(e)
            } finally {
                _pushFeeding.postValue(isCommitSuccessful to map)
            }
        }
    }

    fun deleteByItem(key: Int, index: Int) {
        viewModelScope.launch {
            try {
                sessionFeedingDataStoreManager.deleteItemFromList(key, index)
            } catch (e: Exception) {
                isException.postValue(e)
            }
        }
    }

    fun load(): LiveData<Map<Int, MutableList<ConsumptionRecordItem>>> {
        return sessionFeedingDataStoreManager.loadMap().asLiveData()
    }

    fun isHijauanButtonFilled(blockId: Int): LiveData<Boolean> {
        return sessionFeedingDataStoreManager.isHijauanButtonFilled(blockId).asLiveData()
    }

    fun isKimiaButtonFilled(blockId: Int): LiveData<Boolean> {
        return sessionFeedingDataStoreManager.isKimiaButtonFilled(blockId).asLiveData()
    }

    fun isVitaminButtonFilled(blockId: Int): LiveData<Boolean> {
        return sessionFeedingDataStoreManager.isVitaminButtonFilled(blockId).asLiveData()
    }

    fun isTambahanButtonFilled(blockId: Int): LiveData<Boolean> {
        return sessionFeedingDataStoreManager.isTambahanButtonFilled(blockId).asLiveData()
    }

    // Feeding
    fun clearSessionFeeding(blockId: Int) {
        viewModelScope.launch {
            sessionFeedingDataStoreManager.clearFeeding(blockId)
        }
    }

    // response feeding
    fun createFeedingRecord(blockId: Int, consumptionRecord: List<ConsumptionRecordItem>?) {
        launch(action = {
            val feedingRecordRequest = FeedingRecordRequest(consumptionRecord)
            val response = feedingVtRepository.createFeedingRecord(feedingRecordRequest)
            if (response.isSuccessful) {
                _feedingEmitter.postValue(response.body())
                sessionFeedingDataStoreManager.clearFeeding(blockId)
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }


}