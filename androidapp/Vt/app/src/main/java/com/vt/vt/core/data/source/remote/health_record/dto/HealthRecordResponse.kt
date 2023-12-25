package com.vt.vt.core.data.source.remote.health_record.dto

import com.google.gson.annotations.SerializedName

data class HealthRecordResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("HealthRecordResponse")
    val healthRecordResponse: List<HealthRecordResponseItem?>? = null
)

data class HealthRecordResponseItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("livestock_id")
    val livestockId: Int? = null,

    @field:SerializedName("treatment_methods")
    val treatmentMethods: Any? = null,

    @field:SerializedName("disease_type")
    val diseaseType: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null
)
