package com.vt.vt.ui.daftar_perkawinan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.breeding.BreedingResponse
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import com.vt.vt.core.data.source.remote.breeding.create.CreateBreedingRequest
import com.vt.vt.core.data.source.repository.BreedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ListBreedingViewModel @Inject constructor(private val breedingVtRepository: BreedingVtRepository) :
    BaseViewModel() {

    private val _breedingEmitter = MutableLiveData<List<BreedingResponseItem>>()
    val breedingEmitter: LiveData<List<BreedingResponseItem>> = _breedingEmitter

    private val _createBreedingEmitter = MutableLiveData<BreedingResponse>()
    val createBreedingEmitter: LiveData<BreedingResponse> = _createBreedingEmitter

    private val _deleteBreedingEmitter = MutableLiveData<BreedingResponse>()
    val deleteBreedingEmitter: LiveData<BreedingResponse> = _deleteBreedingEmitter

    fun getAllBreedings() {
        launch(action = {
            val response = breedingVtRepository.getAllBreedings()
            if (response.isSuccessful) {
                _breedingEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

    fun createBreeding(
        createAt: String?,
        livestockMaleId: Int?,
        livestockFemaleId: Int?,
        sledId: Int,
        blockAreaId: Int
    ) {
        launch(action = {
            val breedingRequest = CreateBreedingRequest(
                sledId,
                createAt,
                livestockFemaleId,
                livestockMaleId,
                blockAreaId
            )
            val response = breedingVtRepository.createBreeding(breedingRequest)
            if (response.isSuccessful) {
                _createBreedingEmitter.postValue(response.body())
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

    fun deleteBreedingById(id: String) {
        launch(action = {
            val response = breedingVtRepository.deleteBreedingById(id)
            if (response.isSuccessful) {
                _deleteBreedingEmitter.postValue(response.body())
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