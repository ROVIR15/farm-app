package com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.HistoryPeranakan
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.IHistoryPeranakan
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryAnimalMatingViewModel @Inject constructor(private val historyAnimalMatingRepository: IHistoryPeranakan) :
    ViewModel() {
    private val historyAnimalMatingEmmiter = MutableLiveData<List<HistoryPeranakan>>()
    val historyPeranakan: LiveData<List<HistoryPeranakan>> = historyAnimalMatingEmmiter

    init {
        loadHistoryAnimalMating()
    }

    private fun loadHistoryAnimalMating() {
        historyAnimalMatingEmmiter.value = historyAnimalMatingRepository.getHistoryPeranakan()
    }
}