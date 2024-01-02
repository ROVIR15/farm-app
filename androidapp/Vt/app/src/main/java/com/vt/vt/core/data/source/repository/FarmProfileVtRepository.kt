package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.farm_profile.dto.FarmProfileResponse
import com.vt.vt.core.data.source.remote.upload_image.PostFileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class FarmProfileVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getFarmProfile(): Response<FarmProfileResponse> = apiService.getFarmProfile()
    suspend fun postImageFarmProfile(file: MultipartBody.Part): Response<PostFileResponse> = apiService.postImageFarmProfile(file)
}