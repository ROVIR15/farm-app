package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreaRequest
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponse
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.core.data.source.remote.state_handler.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class VtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getBlockAndAreas(): Response<List<BlockAndAreasResponseItem>> =
        apiService.getBlockAreas()

    suspend fun createBlockAndArea(blockAndAreaRequest: BlockAndAreaRequest): Response<BlockAndAreasResponse> =
        apiService.createBlockArea(blockAndAreaRequest)

    suspend fun deleteBlockAndArea(id: String): Response<BlockAndAreasResponse> =
        apiService.deleteBlockArea(id)
    /* fun getAreasAndBlock(): Flow<ApiResponse<List<BlockAndAreasResponseItem>>> {
         return flow {
             try {
                 val response = apiService.getBlockAreas()
                 println("hello response $response")
                 val data = response.blockAndAreasResponse
                 if (data.isNotEmpty()) {
                     println("hello response 2 ${response.blockAndAreasResponse}")
                     emit(ApiResponse.Success(response.blockAndAreasResponse))
                 } else {
                     println("hello response 3 ${response.blockAndAreasResponse}")
                     emit(ApiResponse.Empty)
                 }
             } catch (e: Exception) {
                 println("hello response 3 ${e.toString()}")
                 emit(ApiResponse.Error(e.toString()))
             }
         }.flowOn(Dispatchers.IO)
     }*/
}