package com.vt.vt.ui.bottom_navigation.keuangan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.keuangan.IPengeluaran
import com.vt.vt.core.data.source.remote.dummy.keuangan.Pengeluaran
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KeuanganViewModel @Inject constructor(private val pengeluaranRepository: IPengeluaran) : ViewModel() {

    private val pengeluaranEmmiter = MutableLiveData<List<Pengeluaran>>()
    val pengeluaranItem: LiveData<List<Pengeluaran>> = pengeluaranEmmiter
    init {
        loadPengeluaran()
    }
    private fun loadPengeluaran(){
        pengeluaranEmmiter.value = pengeluaranRepository.getPengeluaran()
    }
}