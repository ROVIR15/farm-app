package com.vt.vt.ui.file_provider.datakandang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.sleds.model.SledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import com.vt.vt.core.data.source.repository.SledsVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DataKandangViewModel @Inject constructor(private val sledsVtRepository: SledsVtRepository) :
    BaseViewModel() {
    private val _createSled = MutableLiveData<SledsResponse>()
    val createSled: LiveData<SledsResponse> = _createSled

    private val _getSledById = MutableLiveData<SledsResponseItem>()
    val getSledById: LiveData<SledsResponseItem> = _getSledById

    private val _deleteSledsEmitter = MutableLiveData<SledsResponse>()
    val deleteSledById: LiveData<SledsResponse> = _deleteSledsEmitter

    private val _updateSledById = MutableLiveData<SledsResponse>()
    val updateSledById: LiveData<SledsResponse> = _updateSledById

    fun createSled(sledId: Int?, name: String?, description: String?) {
        launch(action = {
            val sledRequest = SledRequest(sledId, name, description)
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

    fun getSledById(id: String) {
        launch(action = {
            val response = sledsVtRepository.getSledById(id)
            if (response.isSuccessful) {
                _getSledById.postValue(response.body())
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun deleteSledById(id: String) {
        launch(action = {
            val response = sledsVtRepository.deleteSledById(id)
            if (response.isSuccessful) {
                _deleteSledsEmitter.postValue(response.body())
                _isDeleted.postValue(Event(response.body()?.message.toString()))
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun updateSledById(id: String, name: String?, description: String?) {
        launch(action = {
            val sledRequest = SledRequest(null, name, description)
            val response = sledsVtRepository.updateSledById(id, sledRequest)
            if (response.isSuccessful) {
                _updateSledById.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message)
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}