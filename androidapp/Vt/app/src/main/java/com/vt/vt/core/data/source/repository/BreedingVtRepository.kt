package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.breeding.BreedingByIdResponse
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import retrofit2.Response
import javax.inject.Inject

class BreedingVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllBreedings(): Response<List<BreedingResponseItem>> = apiService.getBreedings()
    suspend fun getBreedingById(id: String): Response<BreedingByIdResponse> =
        apiService.getBreedingById(id)
}