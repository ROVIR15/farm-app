package com.vt.vt.core.data.source.repository

import android.util.Log
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import retrofit2.Response
import javax.inject.Inject

class FeedingVtRepository @Inject constructor(private val apiService: ApiService) {
    private val stack = mutableListOf<ConsumptionRecordItem>()
    suspend fun createFeedingRecord(feedingRecordRequest: FeedingRecordRequest): Response<FeedingRecordResponse> =
        apiService.createFeedingRecord(feedingRecordRequest)

    fun push(date: String, score: Double, feedCategory: Int, left: Int, skuId: Int, blockAreaId: Int, remarks: String?) {
        val consumptionRecordItem = ConsumptionRecordItem(date, score, feedCategory, left, skuId, blockAreaId, remarks)
        stack.add(consumptionRecordItem)
        Log.d("PFT", "item push : $consumptionRecordItem")
    }

    fun clear() {
        stack.clear()
    }

    fun getAllStack(): List<ConsumptionRecordItem> {
        Log.d("PFT", "stack : $stack")
        return stack
    }
}