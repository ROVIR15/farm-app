package com.vt.vt.ui.rekam_kesehatan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.health_record.dto.HealthRecordRequest
import com.vt.vt.core.data.source.remote.health_record.dto.HealthRecordResponse
import com.vt.vt.core.data.source.repository.HealthRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RekamKesehatanViewModel @Inject constructor(private val healthRecordVtRepository: HealthRecordVtRepository) :
    BaseViewModel() {

    private val _createHealthRecordEmitter = MutableLiveData<HealthRecordResponse>()
    val createHealthRecordEmitter: LiveData<HealthRecordResponse> = _createHealthRecordEmitter

    fun createBcsRecordById(livestockId: Int?, date: String?, remarks: String?) {
        launch(action = {
            val request = HealthRecordRequest(date, livestockId, remarks)
            val response = healthRecordVtRepository.createHealthRecord(request)
            if (response.isSuccessful) {
                _createHealthRecordEmitter.postValue(response.body())
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