package com.vt.vt.ui.rekam_berat_badan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRecordRequest
import com.vt.vt.core.data.source.remote.weight_record.dto.WeightRecordResponse
import com.vt.vt.core.data.source.repository.WeightRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RekamBeratBadanViewModel @Inject constructor(private val weightRecordVtRepository: WeightRecordVtRepository) :
    BaseViewModel() {

    private val _createWeightRecordEmitter = MutableLiveData<WeightRecordResponse>()
    val createWeightRecordEmitter: LiveData<WeightRecordResponse> = _createWeightRecordEmitter

    fun createWeightRecordById(livestockId: Int?, date: String?, score: Double?, remarks: String?) {
        launch(action = {
            val request = LivestockRecordRequest(date, score, livestockId, remarks)
            val response = weightRecordVtRepository.createWeightRecordById(request)
            if (response.isSuccessful) {
                _createWeightRecordEmitter.postValue(response.body())
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