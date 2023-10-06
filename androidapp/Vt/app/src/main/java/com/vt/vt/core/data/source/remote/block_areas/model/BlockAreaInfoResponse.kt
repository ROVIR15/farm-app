package com.vt.vt.core.data.source.remote.block_areas.model

import com.google.gson.annotations.SerializedName

data class BlockAreaInfoResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("info")
    val info: String? = null
)
