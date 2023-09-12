package com.vt.vt.ui.penyimpan_ternak

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.Animal
import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.IAnimal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenyimpanTernakViewModel @Inject constructor(private val animalRepository: IAnimal) :
    ViewModel() {

    private val animalEmitter = MutableLiveData<List<Animal>>()
    val animalName: LiveData<List<Animal>> = animalEmitter

    init {
        loadAnimalName()
    }

    private fun loadAnimalName() {
        animalEmitter.value = animalRepository.getName()
    }
}