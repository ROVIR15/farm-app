package com.vt.vt.core.data.source.remote.livestock.model

import com.google.gson.annotations.SerializedName

data class LivestockResponse(

    @field:SerializedName("livestock_id")
    val livestockId: Int,

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("LivestockResponse")
    val livestockResponse: List<LivestockResponseItem>

)

data class LivestockResponseItem(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("bangsa")
    val bangsa: String,
    @field:SerializedName("gender")
    val gender: Int,
    @field:SerializedName("name")
    val name: String
)

