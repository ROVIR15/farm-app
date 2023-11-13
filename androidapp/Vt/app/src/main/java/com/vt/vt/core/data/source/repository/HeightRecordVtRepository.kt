package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.height_record.HeightRecordResponse
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRecordRequest
import retrofit2.Response
import javax.inject.Inject

class HeightRecordVtRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun createHeightRecordById(livestockRecordRequest: LivestockRecordRequest): Response<HeightRecordResponse> =
        apiService.createHeightRecord(livestockRecordRequest)
}