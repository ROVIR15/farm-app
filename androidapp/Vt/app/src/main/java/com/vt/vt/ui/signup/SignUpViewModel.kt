package com.vt.vt.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.auth.dto.register.request.FarmProfile
import com.vt.vt.core.data.source.remote.auth.dto.register.request.RegisterRequest
import com.vt.vt.core.data.source.remote.auth.dto.register.response.RegisterResponse
import com.vt.vt.core.data.source.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseViewModel() {

    private val _registrationEmitter = MutableLiveData<RegisterResponse?>()
    val isRegistration: LiveData<RegisterResponse?> get() = _registrationEmitter

    fun registration(
        username: String,
        email: String,
        password: String,
        farmProfile: FarmProfile
    ) {
        launch(
            action = {
                val registrationData = RegisterRequest(username, email, password, farmProfile)
                val response = dataRepository.registration(registrationData)
                if (response.isSuccessful) {
                    _registrationEmitter.postValue(response.body())
                } else {
                    val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
                    val message = errorBody.getString("message")
                    isError.postValue(message)
                }
            },
            error = { networkError ->
                if (networkError.isBadRequest) {
                    isError.postValue("Bad Request")
                }
                if (networkError.isInternalServerError) {
                    isError.postValue("Already exist account")
                }
            }
        )
    }
}