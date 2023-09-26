package com.vt.vt.core.data.source.remote.bcs_record.model

import com.google.gson.annotations.SerializedName

data class BcsRecordResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("BcsRecordResponse")
    val bcsRecordResponse: List<BcsRecordResponseItem?>? = null
)

data class BcsRecordResponseItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("score")
    val score: Float? = null,

    @field:SerializedName("livestock_id")
    val livestockId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null
)
