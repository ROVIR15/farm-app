package com.vt.vt.core.data.source.remote.breeding.lambing.create

import com.google.gson.annotations.SerializedName

data class LambingRequest(

	@field:SerializedName("bangsa")
	val bangsa: String? = null,

	@field:SerializedName("gender")
	val gender: Int? = null,

	@field:SerializedName("sled_id")
	val sledId: Int? = null,

	@field:SerializedName("birth_date")
	val birthDate: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("breeding_id")
	val breedingId: Int? = null,

	@field:SerializedName("block_area_id")
	val blockAreaId: Int? = null
)
