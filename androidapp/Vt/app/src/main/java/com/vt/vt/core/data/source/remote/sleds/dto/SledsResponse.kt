package com.vt.vt.core.data.source.remote.sleds.dto

import com.google.gson.annotations.SerializedName

data class SledsResponse(
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("status")
    val status: String?,
// data dibawah ini belum ada
    @field:SerializedName("SledsResponse")
    val sledsResponse: List<SledsResponseItem>
)

data class SledsResponseItem(

    @field:SerializedName("block_area_description")
    val blockAreaDescription: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("block_area_name")
    val blockAreaName: String,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int
)
