package com.vt.vt.core.data.source.remote.weight_record.dto

import com.google.gson.annotations.SerializedName

data class WeightRecordResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("BcsRecordResponse")
    val bcsRecordResponse: List<WeightRecordResponseItem?>? = null
)

data class WeightRecordResponseItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("score")
    val score: Double? = null,

    @field:SerializedName("grow")
    val grow: String = "0 %",

    @field:SerializedName("prev_score")
    val prevScore: Double = 0.0,

    @field:SerializedName("livestock_id")
    val livestockId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null
)
