package com.vt.vt.ui.detail_livestock.tab_layout.beratbadan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.weight_record.dto.WeightRecordResponseItem
import com.vt.vt.core.data.source.repository.WeightRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class WeightRecordViewModel @Inject constructor(private val weightRecordVtRepository: WeightRecordVtRepository) :
    BaseViewModel() {
    private val _weightRecordEmitter = MutableLiveData<List<WeightRecordResponseItem>>()
    val weightRecordEmitter: LiveData<List<WeightRecordResponseItem>> = _weightRecordEmitter

    fun getWeightRecords() {
        launch(action = {
            val response = weightRecordVtRepository.getWeightRecords()
            if (response.isSuccessful) {
                _weightRecordEmitter.postValue(response.body())
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