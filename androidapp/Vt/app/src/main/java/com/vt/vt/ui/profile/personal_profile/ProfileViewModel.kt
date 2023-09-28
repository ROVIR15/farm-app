package com.vt.vt.ui.profile.personal_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.profile.model.ProfileResponse
import com.vt.vt.core.data.source.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseViewModel() {

    private val _getProfileEmitter = MutableLiveData<ProfileResponse>()
    val getProfileEmitter: LiveData<ProfileResponse> = _getProfileEmitter

    fun getProfile() {
        launch(action = {
            val response = dataRepository.getProfile()
            if (response.isSuccessful) {
                _getProfileEmitter.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }
}