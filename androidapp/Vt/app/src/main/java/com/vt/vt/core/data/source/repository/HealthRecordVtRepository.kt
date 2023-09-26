package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponseItem
import retrofit2.Response
import javax.inject.Inject

class HealthRecordVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getHealthRecords(): Response<List<HealthRecordResponseItem>> =
        apiService.getHealthRecords()
}