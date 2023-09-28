package com.vt.vt.ui.bottom_navigation.livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import com.vt.vt.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LivestockViewModel @Inject constructor(private val livestockVtRepository: LivestockVtRepository) :
    BaseViewModel() {

    private val _livestockEmitter = MutableLiveData<List<LivestockResponseItem>>()
    val livestockItems: LiveData<List<LivestockResponseItem>> = _livestockEmitter

    private val _deleteLivestock = MutableLiveData<LivestockResponse>()
    val deleteLivestock: LiveData<LivestockResponse> = _deleteLivestock
    fun getLivestocks() {
        launch(action = {
            val response = livestockVtRepository.getLivestock()
            if (response.isSuccessful) {
                _livestockEmitter.postValue(response.body())
            } else {
                val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                val message = errorBody.getString("message")
                isError.postValue(message.toString())
            }
        }, error = { networkError ->
            isError.postValue("You don't have any livestock")
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun deleteLivestockById(id: String) {
        launch(action = {
            val response = livestockVtRepository.deleteLivestockById(id)
            if (response.isSuccessful) {
                _deleteLivestock.postValue(response.body())
                _isDeleted.postValue(Event(response.body()?.message.toString()))
            } else {
                isError.postValue(response.errorBody().toString())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}