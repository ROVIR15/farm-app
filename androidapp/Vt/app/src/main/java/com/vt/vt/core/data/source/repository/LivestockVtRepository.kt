package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.livestock.model.LivestockByIdResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import retrofit2.Response
import javax.inject.Inject

class LivestockVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getLivestock(): Response<List<LivestockResponseItem>> = apiService.getLivestocks()
    suspend fun createLivestock(livestockRequest: LivestockRequest): Response<LivestockResponse> =
        apiService.createLivestock(livestockRequest)

    suspend fun getLivestockById(id: String): Response<LivestockByIdResponse> =
        apiService.getLivestockById(id)

    suspend fun updateLivestockById(
        id: String,
        livestockRequest: LivestockRequest
    ): Response<LivestockResponse> = apiService.updateLivestockById(id, livestockRequest)

    suspend fun deleteLivestockById(id: String): Response<LivestockResponse> =
        apiService.deleteLivestockById(id)
}