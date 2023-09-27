package com.vt.vt.core.data.source.remote.livestock.model

import com.google.gson.annotations.SerializedName

data class StoreLivestockRequest(

    @field:SerializedName("livestock_id")
    val livestockId: Int,

    @field:SerializedName("sled_id")
    val sledId: Int,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int,
)
