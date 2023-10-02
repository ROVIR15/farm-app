package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import retrofit2.Response
import javax.inject.Inject

class FeedingVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun createFeedingRecord(feedingRecordRequest: FeedingRecordRequest): Response<FeedingRecordResponse> =
        apiService.createFeedingRecord(feedingRecordRequest)
}