package com.vt.vt.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.vt.vt.core.data.local.auth.SessionPreferencesDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val sessionPreferencesDataStoreManager: SessionPreferencesDataStoreManager) :
    ViewModel() {

    val loginState = sessionPreferencesDataStoreManager.getIsLoginState().asLiveData()

    fun saveOnLoginState() {
        CoroutineScope(Dispatchers.IO).launch {
            sessionPreferencesDataStoreManager.saveUserLoginState()
        }
    }

}