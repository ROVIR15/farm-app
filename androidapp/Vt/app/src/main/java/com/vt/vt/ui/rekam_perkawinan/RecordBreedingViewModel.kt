package com.vt.vt.ui.rekam_perkawinan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.breeding.BreedingByIdResponse
import com.vt.vt.core.data.source.remote.breeding.BreedingResponse
import com.vt.vt.core.data.source.remote.breeding.history.create.HistoryBreedingRequest
import com.vt.vt.core.data.source.remote.breeding.lambing.create.LambingRequest
import com.vt.vt.core.data.source.remote.breeding.pregnancy.PregnancyRequest
import com.vt.vt.core.data.source.repository.BreedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordBreedingViewModel @Inject constructor(private val breedingVtRepository: BreedingVtRepository) :
    BaseViewModel() {

    private val _breedingByIdEmitter = MutableLiveData<BreedingByIdResponse>()
    val breedingByIdEmitter: LiveData<BreedingByIdResponse> = _breedingByIdEmitter

    private val _createBreedingEmitter = MutableLiveData<BreedingResponse>()
    val createBreedingEmitter: LiveData<BreedingResponse> = _createBreedingEmitter

    private val _deleteBreedingEmitter = MutableLiveData<BreedingResponse>()
    val deleteBreedingEmitter: LiveData<BreedingResponse> = _deleteBreedingEmitter

    private val _updatePregnancyEmitter = MutableLiveData<BreedingResponse>()
    val updatePregnancyEmitter: LiveData<BreedingResponse> = _updatePregnancyEmitter


    fun getBreedingById(id: String) {
        launch(action = {
            val response = breedingVtRepository.getBreedingById(id)
            if (response.isSuccessful) {
                _breedingByIdEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        },
            error = { networkError -> if (networkError.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun createLambing(breedingId: Int, name: String, gender: Int, bangsa: String, description: String, blockAreaId: Int, sledId: Int, birthDate: String){
        launch(action = {
            val lambingRequest = LambingRequest(bangsa, gender, sledId, birthDate, name, description, breedingId, blockAreaId )
            val response = breedingVtRepository.createLambing(lambingRequest)
            if (response.isSuccessful) {
                _createBreedingEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError -> if (networkError.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun createHistoryBreeding(createAt: String, breedingId: Int, remarks: String){
        launch(action = {
            val historyBreedingRequest = HistoryBreedingRequest(createAt, breedingId, remarks)
            val response = breedingVtRepository.createHistoryBreeding(historyBreedingRequest)
            if (response.isSuccessful) {
                _createBreedingEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError -> if (networkError.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun deleteLambing(id: String){
        launch(action = {
            val response = breedingVtRepository.deleteLambing(id)
            if (response.isSuccessful) {
                _deleteBreedingEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError -> if (networkError.isNetworkError) isError.postValue("No Internet Connection") })
    }

    fun updatePregnancy(id:String, createdAt: String, isActive: Boolean ,remarks: String? ){
        launch(action = {
            val pregnancyRequest = PregnancyRequest(createdAt, isActive, remarks)
            val response = breedingVtRepository.updatePregnancy(id, pregnancyRequest)
            if (response.isSuccessful) {
                _updatePregnancyEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError -> if (networkError.isNetworkError) isError.postValue("No Internet Connection") })
    }
}