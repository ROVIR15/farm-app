package com.vt.vt.ui.edit_livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.livestock.model.LivestockByIdResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponse
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditLivestockViewModel @Inject constructor(private val livestockVtRepository: LivestockVtRepository) :
    BaseViewModel() {
    private val _getLivestockEmitter = MutableLiveData<LivestockByIdResponse?>()
    val getLivestockById: LiveData<LivestockByIdResponse?> = _getLivestockEmitter

    private val _updateLivestock = MutableLiveData<LivestockResponse?>()
    val isUpdateLivestock: LiveData<LivestockResponse?> = _updateLivestock

    fun getLivestockById(id: String) {
        launch(action = {
            val response = livestockVtRepository.getLivestockById(id)
            if (response.isSuccessful) {
                _getLivestockEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Connection Internet")
            }
        })
    }

    fun updateLivestockById(
        id: String,
        name: String?,
        gender: Int,
        nation: String?,
        description: String?, birthDate: String?, parentMaleId: Int, parentFemaleId: Int
    ) {
        launch(action = {
            val livestockRequest = LivestockRequest(
                nation,
                gender,
                name,
                birthDate,
                description,
                parentMaleId,
                parentFemaleId
            )
            val response = livestockVtRepository.updateLivestockById(id, livestockRequest)
            if (response.isSuccessful) {
                _updateLivestock.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}