package com.vt.vt.core.data.source.remote.sleds.dto

import com.google.gson.annotations.SerializedName

data class SledRequest(

    @field:SerializedName("block_area_id")
    val blockAreaId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)
