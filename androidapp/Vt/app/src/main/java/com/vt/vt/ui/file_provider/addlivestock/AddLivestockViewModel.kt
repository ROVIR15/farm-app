package com.vt.vt.ui.file_provider.addlivestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.dto.StoreLivestockRequest
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddLivestockViewModel @Inject constructor(private val livestockVtRepository: LivestockVtRepository) :
    BaseViewModel() {
    private val _createLivestock = MutableLiveData<LivestockResponse>()
    val createLivestock: LiveData<LivestockResponse> = _createLivestock

    private val _storeLivestock = MutableLiveData<LivestockResponse>()
    val storeLivestock: LiveData<LivestockResponse> = _storeLivestock

    fun createLivestock(
        name: String?,
        birthDate: String?,
        description: String?,
        gender: Int,
        bangsa: String?
    ) {
        launch(action = {
            val livestockRequest = LivestockRequest(
                bangsa,
                gender,
                name,
                birthDate,
                description,
                null,
                null
            )
            val response = livestockVtRepository.createLivestock(livestockRequest)
            if (response.isSuccessful) {
                _createLivestock.postValue(response.body())
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

    fun storeLivestock(livestockId: Int, sledId: Int, blockAreaId: Int) {
        launch(action = {
            val storeLivestockRequest = StoreLivestockRequest(livestockId, sledId, blockAreaId)
            val response = livestockVtRepository.storeLivestock(storeLivestockRequest)
            if (response.isSuccessful) {
                _storeLivestock.postValue(response.body())
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