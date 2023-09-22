package com.vt.vt.core.data.source.remote.block_areas.model

import com.google.gson.annotations.SerializedName

data class BlockAndAreaRequest(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)
