package com.vt.vt.ui.bottom_navigation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val sessionPreferencesDataStoreManager: SessionPreferencesDataStoreManager) :
    ViewModel() {
    fun logout() {
        viewModelScope.launch {
            sessionPreferencesDataStoreManager.removeLoginState()
        }
    }
}