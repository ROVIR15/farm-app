package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRecordRequest
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponse
import retrofit2.Response
import javax.inject.Inject

class MilkProductionVtRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun createMilkProductionRecord(livestockRecordRequest: LivestockRecordRequest): Response<LivestockResponse> =
        apiService.createMilkRecord(livestockRecordRequest)
}