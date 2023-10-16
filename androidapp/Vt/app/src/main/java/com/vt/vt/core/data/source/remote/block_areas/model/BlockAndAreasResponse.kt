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

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("sleds")
    val sleds: List<Sleds>? = null,

    @field:SerializedName("livestock")
    val livestocks: List<LivestockResponseItem>? = null,

    @field:SerializedName("feeding_records")
    val feedingRecords: List<FeedingRecordsItem>? = null,
)

data class FeedListItem(

    @field:SerializedName("feed_category")
    val feedCategory: String? = null,

    @field:SerializedName("total_score")
    val totalScore: Double? = null
)

data class FeedingRecordsItem(

    @field:SerializedName("block_area_id")
    val blockAreaId: Int? = null,

    @field:SerializedName("day")
    val day: String? = null,

    @field:SerializedName("feed_list")
    val feedList: List<FeedListItem>
)

data class Sleds(

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("block_area_id")
    val blockAreaId: Int
)