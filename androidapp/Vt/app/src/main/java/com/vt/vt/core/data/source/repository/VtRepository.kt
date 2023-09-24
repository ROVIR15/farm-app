package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import retrofit2.Response
import javax.inject.Inject

class VtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getBlockAndAreas(): Response<List<BlockAndAreasResponseItem>> =
        apiService.getBlockAreas()

    suspend fun createBlockAndArea(blockAndAreaRequest: BlockAndAreaRequest): Response<BlockAndAreasResponse> =
        apiService.createBlockArea(blockAndAreaRequest)

    suspend fun deleteBlockAndArea(id: String): Response<BlockAndAreasResponse> =
        apiService.deleteBlockArea(id)

    suspend fun getBlockAndArea(id: String) = apiService.getBlockArea(id)

    suspend fun updateBlockAndArea(
        id: String,
        blockAndAreaRequest: BlockAndAreaRequest
    ): Response<BlockAndAreasResponse> = apiService.updateBlockArea(
        id, blockAndAreaRequest
    )
}