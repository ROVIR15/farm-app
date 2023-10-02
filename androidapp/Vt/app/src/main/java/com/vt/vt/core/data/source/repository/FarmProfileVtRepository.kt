package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.farm_profile.model.FarmProfileResponse
import retrofit2.Response
import javax.inject.Inject

class FarmProfileVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getFarmProfile(): Response<FarmProfileResponse> = apiService.getFarmProfile()
}