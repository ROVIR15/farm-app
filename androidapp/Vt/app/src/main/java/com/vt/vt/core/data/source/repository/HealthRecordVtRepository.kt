package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.bcs_record.model.BcsRecordResponse
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordRequest
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponse
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponseItem
import com.vt.vt.core.data.source.remote.livestock.model.LivestockRecordRequest
import retrofit2.Response
import javax.inject.Inject

class HealthRecordVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getHealthRecords(): Response<List<HealthRecordResponseItem>> =
        apiService.getHealthRecords()

    suspend fun createHealthRecord(healthRecordRequest: HealthRecordRequest): Response<HealthRecordResponse> =
        apiService.createHealthRecord(healthRecordRequest)
}