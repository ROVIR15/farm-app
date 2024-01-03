package com.vt.vt.ui.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordRequest
import com.vt.vt.core.data.source.remote.auth.dto.change_password.ChangePasswordResponse
import com.vt.vt.core.data.source.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseViewModel() {
    private val _changePasswordEmitter = MutableLiveData<ChangePasswordResponse?>()
    val changePasswordEmitter: LiveData<ChangePasswordResponse?> = _changePasswordEmitter

    fun doChangePassword(
        username: String,
        password: String,
        newPassword: String
    ) {
        launch(
            action = {
                val changePasswordRequest = ChangePasswordRequest(
                    username = username,
                    password = password,
                    newPassword = newPassword
                )
                val response = dataRepository.doChangePassword(changePasswordRequest)
                if (response.isSuccessful) {
                    _changePasswordEmitter.postValue(response.body())
                    delay(300)
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