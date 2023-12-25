package com.vt.vt.ui.detail_livestock.tab_layout.bcs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.bcs_record.dto.BcsRecordResponseItem
import com.vt.vt.core.data.source.repository.BcsRecordVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class BcsViewModel @Inject constructor(private val bcsRecordVtRepository: BcsRecordVtRepository) :
    BaseViewModel() {
    private val _bcsEmitter = MutableLiveData<List<BcsRecordResponseItem>>()
    val bcsEmitter: LiveData<List<BcsRecordResponseItem>> = _bcsEmitter

    fun getBcsRecords() {
        launch(action = {
            val response = bcsRecordVtRepository.getBcsRecords()
            if (response.isSuccessful) {
                _bcsEmitter.postValue(response.body())
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