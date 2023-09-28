package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.sleds.model.SledRequest
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import retrofit2.Response
import javax.inject.Inject

class SledsVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSleds(): Response<List<SledsResponseItem>> = apiService.getAllSleds()
    suspend fun deleteSledById(id: String): Response<SledsResponse> = apiService.deleteSledById(id)
    suspend fun getSledById(id: String): Response<SledsResponseItem> = apiService.getSledById(id)
    suspend fun createSled(sledRequest: SledRequest): Response<SledsResponse> =
        apiService.createSled(sledRequest)

    suspend fun updateSledById(id: String, sledRequest: SledRequest): Response<SledsResponse> =
        apiService.updateSled(id, sledRequest)
}