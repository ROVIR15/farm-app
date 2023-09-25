package com.vt.vt.ui.file_provider.datakandang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.sleds.model.SledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.repository.SledsVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DataKandangViewModel @Inject constructor(private val sledsVtRepository: SledsVtRepository) :
    BaseViewModel() {
    private val _createSled = MutableLiveData<SledsResponse>()
    val createSled: LiveData<SledsResponse> = _createSled
    fun createSled(blockAreaId: Int?, name: String?, description: String?) {
        launch(action = {
            val sledRequest = SledRequest(blockAreaId, name, description)
            val response = sledsVtRepository.createSled(sledRequest)
            if (response.isSuccessful) {
                _createSled.postValue(response.body())
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