package com.vt.vt.ui.bottom_navigation.profile

import androidx.lifecycle.ViewModel
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val sessionPreferencesDataStoreManager: SessionPreferencesDataStoreManager) :
    ViewModel() {

    fun removeLoginState() {
        CoroutineScope(Dispatchers.IO).launch {
            sessionPreferencesDataStoreManager.removeLoginState()
        }
    }
}