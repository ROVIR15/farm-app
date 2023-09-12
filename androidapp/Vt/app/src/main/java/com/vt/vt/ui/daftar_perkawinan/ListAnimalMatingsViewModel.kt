package com.vt.vt.ui.daftar_perkawinan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatings
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.IAnimalMatings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListAnimalMatingsViewModel @Inject constructor(private val animalMatingsRepositoryImpl: IAnimalMatings) :
    ViewModel() {
    private val animalMatingsEmitter = MutableLiveData<List<AnimalMatings>>()
    val animalMatings: LiveData<List<AnimalMatings>> = animalMatingsEmitter

    init {
        getAnimalMatings()
    }

    private fun getAnimalMatings() {
        animalMatingsEmitter.value = animalMatingsRepositoryImpl.getAnimalMaatings()
    }
}