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
import com.vt.vt.core.data.source.repository.FeedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PemberianTernakViewModel @Inject constructor(
    private val sessionFeedingDataStoreManager: SessionFeedingDataStoreManager,
    private val feedingVtRepository: FeedingVtRepository
) : BaseViewModel() {

    private val _stack = MutableLiveData<Map<Int, List<ConsumptionRecordItem>>>()
    val stack: LiveData<Map<Int, List<ConsumptionRecordItem>>> = _stack

    private val _feedingEmitter = MutableLiveData<FeedingRecordResponse>()
    val feedingEmitter: LiveData<FeedingRecordResponse> = _feedingEmitter

    init {
        _stack.value = feedingVtRepository.getAllStack()
    }

    // Button State for Feeding
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
    fun clearSessionFeeding() {
        viewModelScope.launch {
            sessionFeedingDataStoreManager.clearFeedingStates()
        }
    }

    // stack feeding
    fun addStack(
        blockId: Int,
        date: String,
        score: Double,
        feedCategory: Int,
        left: Int,
        skuId: Int,
        blockAreaId: Int,
        remarks: String?
    ) {
        feedingVtRepository.push(
            blockId,
            date,
            score,
            feedCategory,
            left,
            skuId,
            blockAreaId,
            remarks
        )
        _stack.value = feedingVtRepository.getAllStack()
    }

    fun clear() {
        feedingVtRepository.clear()
        _stack.value = feedingVtRepository.getAllStack()
    }

    // response feeding
    fun createFeedingRecord(consumptionRecord: List<ConsumptionRecordItem>?) {
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