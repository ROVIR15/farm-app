package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.bcs_record.dto.BcsRecordResponse
import com.vt.vt.core.data.source.remote.bcs_record.dto.BcsRecordResponseItem
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockRecordRequest
import retrofit2.Response
import javax.inject.Inject

class BcsRecordVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getBcsRecords(): Response<List<BcsRecordResponseItem>> = apiService.getBcsRecords()
    suspend fun createBcsRecordById(livestockRecordRequest: LivestockRecordRequest): Response<BcsRecordResponse> =
        apiService.createBcsRecord(livestockRecordRequest)
}