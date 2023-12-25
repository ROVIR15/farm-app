package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.sleds.dto.MoveSledRequest
import com.vt.vt.core.data.source.remote.sleds.dto.SledOptionResponseItem
import com.vt.vt.core.data.source.remote.sleds.dto.SledRequest
import com.vt.vt.core.data.source.remote.sleds.dto.SledsResponse
import com.vt.vt.core.data.source.remote.sleds.dto.SledsResponseItem
import retrofit2.Response
import javax.inject.Inject

class SledsVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSleds(): Response<List<SledsResponseItem>> = apiService.getAllSleds()
    suspend fun getSledOptions(): Response<List<SledOptionResponseItem>> =
        apiService.getSledOptions()

    suspend fun deleteSledById(id: String): Response<SledsResponse> = apiService.deleteSledById(id)
    suspend fun getSledById(id: String): Response<SledsResponseItem> = apiService.getSledById(id)
    suspend fun createSled(sledRequest: SledRequest): Response<SledsResponse> =
        apiService.createSled(sledRequest)

    suspend fun moveSledToBlockArea(
        id: String,
        moveSledRequest: MoveSledRequest
    ): Response<SledsResponse> = apiService.sledMoveToBlockArea(id, moveSledRequest)

    suspend fun updateSledById(id: String, sledRequest: SledRequest): Response<SledsResponse> =
        apiService.updateSled(id, sledRequest)
}