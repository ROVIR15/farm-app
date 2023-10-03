package com.vt.vt.ui.daftar_perkawinan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatings
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.IAnimalMatings
import com.vt.vt.core.data.source.repository.BreedingVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListAnimalMatingsViewModel @Inject constructor(private val breedingVtRepository: BreedingVtRepository) :
    BaseViewModel() {

    private val _breedingEmitter = MutableLiveData<List<BreedingResponseItem?>>()
    val breedingEmitter: LiveData<List<BreedingResponseItem?>> = _breedingEmitter

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
}