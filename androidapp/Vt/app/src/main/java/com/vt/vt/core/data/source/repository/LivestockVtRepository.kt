package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockByIdResponse
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockMoveSledRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockOptionResponseItem
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponse
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponseItem
import com.vt.vt.core.data.source.remote.livestock.dto.StoreLivestockRequest
import retrofit2.Response
import javax.inject.Inject

class LivestockVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getLivestock(): Response<List<LivestockResponseItem>> = apiService.getLivestocks()
    suspend fun searchLivestock(query: String?): Response<List<LivestockResponseItem>> =
        apiService.getListBySearch(query)

    suspend fun createLivestock(livestockRequest: LivestockRequest): Response<LivestockResponse> =
        apiService.createLivestock(livestockRequest)

    suspend fun livestockMoveSled(livestockMoveSledRequest: LivestockMoveSledRequest): Response<LivestockResponse> =
        apiService.livestockMoveToSled(livestockMoveSledRequest)

    suspend fun storeLivestock(storeLivestockRequest: StoreLivestockRequest): Response<LivestockResponse> =
        apiService.storeLivestock(storeLivestockRequest)

    suspend fun getLivestockById(id: String): Response<LivestockByIdResponse> =
        apiService.getLivestockById(id)

    suspend fun updateLivestockById(
        id: String,
        livestockRequest: LivestockRequest
    ): Response<LivestockResponse> = apiService.updateLivestockById(id, livestockRequest)

    suspend fun deleteLivestockById(id: String): Response<LivestockResponse> =
        apiService.deleteLivestockById(id)

    suspend fun getLivestocksMale(): Response<List<LivestockResponseItem>> =
        apiService.getLivestocksMale()

    suspend fun getLivestocksFemale(): Response<List<LivestockResponseItem>> =
        apiService.getLivestocksFemale()

    suspend fun getOptionLivestock(): Response<List<LivestockOptionResponseItem>> =
        apiService.getListOptionLivestock()
}