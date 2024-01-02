package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.block_areas.dto.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.dto.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.dto.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.remote.upload_image.PostFileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class BlockAndAreasVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getBlockAndAreas(): Response<List<BlockAndAreasResponseItem>> =
        apiService.getBlockAreas()

    suspend fun postImageBlockArea(file: MultipartBody.Part): Response<PostFileResponse> =
        apiService.postImageBlockArea(file)

    suspend fun createBlockAndArea(
        blockAndAreaRequest: BlockAndAreaRequest
    ): Response<BlockAndAreasResponse> =
        apiService.createBlockArea(blockAndAreaRequest)

    suspend fun deleteBlockAndArea(id: String): Response<BlockAndAreasResponse> =
        apiService.deleteBlockArea(id)

    suspend fun getBlockAndArea(id: String) = apiService.getBlockArea(id)
    suspend fun getBlockAndAreaInfoById(id: String) = apiService.getBlockAreaInfo(id)

    suspend fun updateBlockAndArea(
        id: String,
        blockAndAreaRequest: BlockAndAreaRequest
    ): Response<BlockAndAreasResponse> = apiService.updateBlockArea(
        id, blockAndAreaRequest
    )
}