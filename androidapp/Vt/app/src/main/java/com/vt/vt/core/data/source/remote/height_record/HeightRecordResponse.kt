package com.vt.vt.core.data.source.remote.height_record

import com.google.gson.annotations.SerializedName

data class HeightRecordResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?
)