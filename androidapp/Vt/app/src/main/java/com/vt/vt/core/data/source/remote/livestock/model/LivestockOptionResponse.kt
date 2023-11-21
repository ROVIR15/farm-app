package com.vt.vt.core.data.source.remote.livestock.model

import com.google.gson.annotations.SerializedName

data class LivestockOptionResponse(

	@field:SerializedName("LivestockOptionResponse")
	val livestockOptionResponse: List<LivestockOptionResponseItem>? = null
)

data class LivestockOptionResponseItem(

	@field:SerializedName("bangsa")
	val bangsa: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("info")
	val info: String? = null
)
