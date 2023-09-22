package com.vt.vt.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.auth.model.login.LoginRequest
import com.vt.vt.core.data.source.remote.auth.model.login.LoginResponse
import com.vt.vt.core.data.source.remote.auth.model.user_session.UserSession
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import com.vt.vt.core.data.source.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val sessionPreferencesDataStoreManager: SessionPreferencesDataStoreManager,
    private val dataRepository: DataRepository
) :
    BaseViewModel() {
    val loginState = sessionPreferencesDataStoreManager.getLoginSession().asLiveData()

    private val _loginEmitter = MutableLiveData<LoginResponse?>()
    val isLogin: LiveData<LoginResponse?> get() = _loginEmitter

    fun login(username: String, password: String) {
        launch(
            action = {
                val loginRequest = LoginRequest(username, password)
                val response = dataRepository.login(loginRequest)
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    _loginEmitter.postValue(response.body())
                    val userSession = UserSession(username, token!!, true)
                    sessionPreferencesDataStoreManager.saveUserLoginState(userSession)
                } else {
                    val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                    val message = errorBody.getString("message")
                    isError.postValue(message)
                }
            },
            error = { networkError ->
                if (networkError.isAuthFailure) {
                    isError.postValue("Please enter correct Email and Password")
                }
                if (networkError.isNetworkError) {
                    isError.postValue("No Internet Connection")
                }
            }
        )
    }
}