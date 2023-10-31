package com.vt.vt.core.data.source.repository

import android.util.Log
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordRequest
import com.vt.vt.core.data.source.remote.feeding_record.model.FeedingRecordResponse
import retrofit2.Response
import javax.inject.Inject

class FeedingVtRepository @Inject constructor(private val apiService: ApiService) {
    private val stack = mutableListOf<Int>()
    suspend fun createFeedingRecord(feedingRecordRequest: FeedingRecordRequest): Response<FeedingRecordResponse> =
        apiService.createFeedingRecord(feedingRecordRequest)

    fun push(item: Int) {
        stack.add(item)
        Log.d("PFT", "item push : $item")
    }

    fun clear() {
        stack.clear()
    }

    fun getAllStack(): List<Int> {
        Log.d("PFT", "stack : $stack")
        return stack
    }
}