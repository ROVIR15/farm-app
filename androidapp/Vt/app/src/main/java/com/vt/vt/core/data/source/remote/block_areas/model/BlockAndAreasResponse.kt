package com.vt.vt.core.data.source.remote.block_areas.model

import com.google.gson.annotations.SerializedName

data class BlockAndAreasResponse(
	@field:SerializedName("status")
	val status: String?,

	@field:SerializedName("BlockAndAreasResponse")
	val blockAndAreasResponse: List<BlockAndAreasResponseItem>

)

data class BlockAndAreasResponseItem(
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("description")
	val description: String,
	@field:SerializedName("id")
	val id: Int
)
