package com.vt.vt.ui.pemberian_ternak.tambahan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.session_manager.SessionFeedingDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TambahanViewModel @Inject constructor(private val sessionFeedingDataStoreManager: SessionFeedingDataStoreManager) :
    ViewModel() {

    fun setButtonTambahan(blockId: Int, isFilled: Boolean) {
        viewModelScope.launch {
            sessionFeedingDataStoreManager.setTambahanButtonFilled(blockId, isFilled)
        }
    }
}