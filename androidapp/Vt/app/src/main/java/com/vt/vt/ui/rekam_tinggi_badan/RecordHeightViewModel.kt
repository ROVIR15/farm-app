package com.vt.vt.ui.rekam_tinggi_badan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.height_record.HeightRecordResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRecordRequest
import com.vt.vt.core.data.source.repository.HeightRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RecordHeightViewModel @Inject constructor(private val heightRecordVtRepository: HeightRecordVtRepository) :
    BaseViewModel() {

    private val _createHeightRecordEmitter = MutableLiveData<HeightRecordResponse>()
    val createHeightRecordEmitter: LiveData<HeightRecordResponse> = _createHeightRecordEmitter

    fun createHeightRecordById(livestockId: Int?, date: String?, score: Double?, remarks: String?) {
        launch(action = {
            val request = LivestockRecordRequest(date, score, livestockId, remarks)
            val response = heightRecordVtRepository.createHeightRecordById(request)
            if (response.isSuccessful) {
                _createHeightRecordEmitter.postValue(response.body())
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