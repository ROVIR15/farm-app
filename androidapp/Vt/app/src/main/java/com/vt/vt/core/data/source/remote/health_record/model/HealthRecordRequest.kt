package com.vt.vt.core.data.source.remote.health_record.model

import com.google.gson.annotations.SerializedName

data class HealthRecordRequest(

    @field: SerializedName("date")
    val date: String? = null,

    @field:SerializedName("livestock_id")
    val livestockId: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null
)

