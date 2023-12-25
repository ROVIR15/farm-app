package com.vt.vt.ui.detail_livestock.tab_layout.kesehatan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.health_record.dto.HealthRecordResponseItem
import com.vt.vt.core.data.source.repository.HealthRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HealthRecordViewModel @Inject constructor(private val healthRecordVtRepository: HealthRecordVtRepository) :
    BaseViewModel() {
    private val _healthRecordsEmitter = MutableLiveData<List<HealthRecordResponseItem>>()
    val healthRecordsEmitter: LiveData<List<HealthRecordResponseItem>> = _healthRecordsEmitter

    fun getListHealthRecords() {
        launch(action = {
            val response = healthRecordVtRepository.getHealthRecords()
            if (response.isSuccessful) {
                _healthRecordsEmitter.postValue(response.body())
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