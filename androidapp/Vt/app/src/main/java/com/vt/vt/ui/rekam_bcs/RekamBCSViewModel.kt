package com.vt.vt.ui.rekam_bcs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.bcs_record.dto.BcsRecordResponse
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRecordRequest
import com.vt.vt.core.data.source.repository.BcsRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RekamBCSViewModel @Inject constructor(private val bcsRecordVtRepository: BcsRecordVtRepository) :
    BaseViewModel() {

    private val _createBcsRecordEmitter = MutableLiveData<BcsRecordResponse>()
    val createBcsRecordEmitter: LiveData<BcsRecordResponse> = _createBcsRecordEmitter

    fun createBcsRecordById(livestockId: Int?, date: String?, score: Double?, remarks: String?) {
        launch(action = {
            val request = LivestockRecordRequest(date, score, livestockId, remarks)
            val response = bcsRecordVtRepository.createBcsRecordById(request)
            if (response.isSuccessful) {
                _createBcsRecordEmitter.postValue(response.body())
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