package com.vt.vt.core.data.source.remote.feeding_record.model

import com.google.gson.annotations.SerializedName

data class FeedingRecordResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,
)
