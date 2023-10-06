package com.vt.vt.core.data.source.remote.block_areas.model

import com.google.gson.annotations.SerializedName
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem

data class BlockAndAreasResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("BlockAndAreasResponse")
    val blockAndAreasResponse: List<BlockAndAreasResponseItem>

)

data class BlockAndAreasResponseItem(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("sleds")
    val sleds: List<Sleds>? = null,

    @field:SerializedName("livestock")
    val livestocks: List<LivestockResponseItem>? = null
)

data class Sleds(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int
)