package com.vt.vt.ui.bottom_navigation.livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.local.livestock.ILivestock
import com.vt.vt.core.data.local.livestock.Livestock
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LivestockViewModel @Inject constructor(private val livestockRepositoryImpl: ILivestock) :
    ViewModel() {

    private val livestockEmitter = MutableLiveData<List<Livestock>>()
    val livestockItems: LiveData<List<Livestock>> = livestockEmitter

    init {
        loadLivestockItems()
    }

    private fun loadLivestockItems() {
        livestockEmitter.value = livestockRepositoryImpl.getLivestock()
    }
}