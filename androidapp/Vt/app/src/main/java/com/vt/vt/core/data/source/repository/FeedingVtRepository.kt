package com.vt.vt.core.data.source.repository

import android.util.Log
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import retrofit2.Response
import javax.inject.Inject

class FeedingVtRepository @Inject constructor(private val apiService: ApiService) {
    private val stacks = mutableMapOf<Int, MutableList<ConsumptionRecordItem>>()
    suspend fun createFeedingRecord(feedingRecordRequest: FeedingRecordRequest): Response<FeedingRecordResponse> =
        apiService.createFeedingRecord(feedingRecordRequest)

    fun push(
        stackFromBlockId: Int,
        date: String,
        score: Double,
        feedCategory: Int,
        left: Int,
        skuId: Int,
        blockAreaId: Int,
        remarks: String?
    ) {
        val consumptionRecordItem =
            ConsumptionRecordItem(date, score, feedCategory, left, skuId, blockAreaId, remarks)
        if (!stacks.contains(stackFromBlockId)) {
            stacks[stackFromBlockId] = mutableListOf()
        }
        stacks[stackFromBlockId]?.add(consumptionRecordItem)
        Log.d("PFT", "item push : $consumptionRecordItem")
    }

    fun clear() {
        stacks.clear()
    }

    fun getAllStack(): Map<Int, List<ConsumptionRecordItem>> {
        Log.d("PFT", "stack : $stacks")
        return stacks
    }
}