package com.vt.vt.core.data.source.remote.sleds.model

import com.google.gson.annotations.SerializedName

data class SledOptionResponse(

    @field:SerializedName("SledOptionResponse")
    val sledOptionResponse: List<SledOptionResponseItem>? = null
)
data class SledOptionResponseItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("block_area_name")
    val blockAreaName: String? = null,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int? = null,

    @field:SerializedName("info")
    val info: String? = null
)
