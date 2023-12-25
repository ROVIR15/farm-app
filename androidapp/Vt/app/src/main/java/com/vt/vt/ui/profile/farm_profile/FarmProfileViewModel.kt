package com.vt.vt.ui.profile.farm_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.farm_profile.dto.FarmProfileResponse
import com.vt.vt.core.data.source.repository.FarmProfileVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FarmProfileViewModel @Inject constructor(private val farmProfileVtRepository: FarmProfileVtRepository) :
    BaseViewModel() {
    private val _farmProfileEmitter = MutableLiveData<FarmProfileResponse>()
    val farmProfileEmitter: LiveData<FarmProfileResponse> = _farmProfileEmitter

    fun getFarmProfile() {
        launch(action = {
            val response = farmProfileVtRepository.getFarmProfile()
            if (response.isSuccessful) {
                _farmProfileEmitter.postValue(response.body())
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