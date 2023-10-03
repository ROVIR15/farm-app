package com.vt.vt.ui.bottom_navigation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import com.vt.vt.core.data.source.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionPreferencesDataStoreManager: SessionPreferencesDataStoreManager,
    private val dataRepository: DataRepository
) :
    BaseViewModel() {
    private val _logoutEmitter = MutableLiveData<LoginResponse?>()
    val isLogout: LiveData<LoginResponse?> = _logoutEmitter

    fun logout() {
        launch(
            action = {
                val response = dataRepository.logout()
                if (response.isSuccessful) {
                    _logoutEmitter.postValue(response.body())
                    sessionPreferencesDataStoreManager.removeLoginState()
                } else {
                    val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                    val message = errorBody.getString("message")
                    isError.postValue(message)
                }
            },
            error = { networkError ->
                if (networkError.isNetworkError) {
                    isError.postValue("No Internet Connection")
                }
                if (networkError.isBadRequest) {
                    isError.postValue("Your request couldn't be processed due to incorrect or missing information")
                }
            }
        )
    }
}