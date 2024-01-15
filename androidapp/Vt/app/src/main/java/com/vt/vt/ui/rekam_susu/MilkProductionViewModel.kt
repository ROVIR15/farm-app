package com.vt.vt.ui.rekam_susu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRecordRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponse
import com.vt.vt.core.data.source.repository.MilkProductionVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MilkProductionViewModel @Inject constructor(private val milkProductionVtRepository: MilkProductionVtRepository) :
    BaseViewModel() {

    private val _createMilkProductionEmitter = MutableLiveData<LivestockResponse>()
    val createMilkProductionEmitter: LiveData<LivestockResponse> = _createMilkProductionEmitter

    fun createMilkProduction(livestockId: Int?, date: String?, score: Double?, remarks: String?) {
        launch(action = {
            val request = LivestockRecordRequest(date, score, livestockId, remarks)
            val response = milkProductionVtRepository.createMilkProductionRecord(request)
            if (response.isSuccessful) {
                _createMilkProductionEmitter.postValue(response.body())
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