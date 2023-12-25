package com.vt.vt.core.data.source.remote.livestock.dto

import com.google.gson.annotations.SerializedName

data class LivestockRecordRequest(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("score")
    val score: Double? = null,

    @field:SerializedName("livestock_id")
    val livestockId: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null
)