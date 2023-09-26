package com.vt.vt.core.data.source.repository

import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.weight_record.model.WeightRecordResponseItem
import retrofit2.Response
import javax.inject.Inject

class WeightRecordVtRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getWeightRecords(): Response<List<WeightRecordResponseItem>> =
        apiService.getWeightRecords()
}