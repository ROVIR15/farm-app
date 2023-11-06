package com.vt.vt.ui.pemberian_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.dummy.SessionFeedingDataStoreManager
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import com.vt.vt.core.data.source.repository.BlockAndAreasVtRepository
import com.vt.vt.core.data.source.repository.FeedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PemberianTernakViewModel @Inject constructor(
    private val blockAndAreasVtRepository: BlockAndAreasVtRepository,
    private val sessionFeedingDataStoreManager: SessionFeedingDataStoreManager,
    private val feedingVtRepository: FeedingVtRepository
) : BaseViewModel() {

    val isHijauanFilled = sessionFeedingDataStoreManager.isHijauanButtonFilled().asLiveData()
    val isKimiaFilled = sessionFeedingDataStoreManager.isKimiaButtonFilled().asLiveData()
    val isVitaminFilled = sessionFeedingDataStoreManager.isVitaminButtonFilled().asLiveData()
    val isTambahanFilled = sessionFeedingDataStoreManager.isTambahanButtonFilled().asLiveData()

    private val _stack = MutableLiveData<List<ConsumptionRecordItem>>()
    val stack: LiveData<List<ConsumptionRecordItem>> = _stack

    private val _feedingEmitter = MutableLiveData<FeedingRecordResponse>()
    val feedingEmitter: LiveData<FeedingRecordResponse> = _feedingEmitter

    init {
        _stack.value = feedingVtRepository.getAllStack()
    }

    // Feeding
    fun clearSessionFeeding() {
        viewModelScope.launch {
            sessionFeedingDataStoreManager.clearFeedingStates()
        }
    }

    // stack feeding
    fun addStack(
        date: String,
        score: Double,
        feedCategory: Int,
        left: Int,
        skuId: Int,
        blockAreaId: Int,
        remarks: String?
    ) {
        feedingVtRepository.push(date, score, feedCategory, left, skuId, blockAreaId, remarks)
        _stack.value = feedingVtRepository.getAllStack()
    }

    fun clear() {
        feedingVtRepository.clear()
        _stack.value = feedingVtRepository.getAllStack()
    }

    // response feeding
    fun createFeedingRecord(consumptionRecord: List<ConsumptionRecordItem>) {
        launch(action = {
            val feedingRecordRequest = FeedingRecordRequest(consumptionRecord)
            val response = feedingVtRepository.createFeedingRecord(feedingRecordRequest)
            if (response.isSuccessful) {
                _feedingEmitter.postValue(response.body())
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