package com.vt.vt.core.data.source.remote.sleds.dto

import com.google.gson.annotations.SerializedName

data class MoveSledRequest(
    @field:SerializedName("block_area_id")
    val blockAreaId: Int? = null,
)