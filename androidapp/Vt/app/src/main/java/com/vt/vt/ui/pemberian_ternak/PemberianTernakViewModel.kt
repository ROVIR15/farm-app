package com.vt.vt.ui.pemberian_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import com.vt.vt.core.data.source.repository.FeedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PemberianTernakViewModel @Inject constructor(private val feedingVtRepository: FeedingVtRepository) :
    BaseViewModel() {

    private val _feedingEmitter = MutableLiveData<FeedingRecordResponse>()
    val feedingEmitter: LiveData<FeedingRecordResponse> = _feedingEmitter

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