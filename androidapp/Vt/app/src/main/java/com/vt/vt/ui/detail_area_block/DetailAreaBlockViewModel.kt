package com.vt.vt.ui.detail_area_block

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.AnimalCage
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.IAnimalCage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailAreaBlockViewModel @Inject constructor(private val animalCageRepositoryImpl: IAnimalCage) :
    ViewModel() {

    private val animalCageEmitter = MutableLiveData<List<AnimalCage>>()
    val animalCageItems: LiveData<List<AnimalCage>> = animalCageEmitter

    init {
        loadListAnimalCage()
    }

    private fun loadListAnimalCage() {
        animalCageEmitter.value = animalCageRepositoryImpl.getListAnimalCage()
    }
}