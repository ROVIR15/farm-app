package com.vt.vt.ui.pemberian_ternak.kimia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.source.remote.dummy.SessionFeedingDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KimiaViewModel @Inject constructor(private val sessionFeedingDataStoreManager: SessionFeedingDataStoreManager) :
    ViewModel() {

    fun setButtonKimia(blockId: Int, isFilled: Boolean) {
        viewModelScope.launch {
            sessionFeedingDataStoreManager.setKimiaButtonFilled(blockId, isFilled)
        }
    }
}