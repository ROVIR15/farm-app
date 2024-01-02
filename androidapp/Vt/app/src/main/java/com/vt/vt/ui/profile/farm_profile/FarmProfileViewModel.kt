package com.vt.vt.ui.profile.farm_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.vt.vt.core.data.source.base.BaseViewModel
import com.vt.vt.core.data.source.remote.farm_profile.dto.FarmProfileResponse
import com.vt.vt.core.data.source.remote.upload_image.PostFileResponse
import com.vt.vt.core.data.source.repository.FarmProfileVtRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class FarmProfileViewModel @Inject constructor(private val farmProfileVtRepository: FarmProfileVtRepository) :
    BaseViewModel() {
    private val _farmProfileEmitter = MutableLiveData<FarmProfileResponse>()
    val farmProfileEmitter: LiveData<FarmProfileResponse> = _farmProfileEmitter

    private val _postImageFarmProfile = MutableLiveData<PostFileResponse>()
    val postImageFarmProfile: LiveData<PostFileResponse> = _postImageFarmProfile
    fun postImageFarmProfile(file: MultipartBody.Part) {
        launch(action = {
            val response = farmProfileVtRepository.postImageFarmProfile(file)
            if (response.isSuccessful) {
                _postImageFarmProfile.postValue(response.body())
            } else {
                isError.postValue(response.message())
            }
        }, error = { networkError ->
            if (networkError.isNetworkError) {
                isError.postValue("No Internet Connection")
            }
        })
    }

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