package com.vt.vt.ui.rekam_perkawinan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.breeding.BreedingByIdResponse
import com.vt.vt.core.data.source.repository.BreedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordBreedingViewModel @Inject constructor(private val breedingVtRepository: BreedingVtRepository) :
    BaseViewModel() {

    private val _breedingByIdEmitter = MutableLiveData<BreedingByIdResponse>()
    val breedingByIdEmitter: LiveData<BreedingByIdResponse> = _breedingByIdEmitter

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

}