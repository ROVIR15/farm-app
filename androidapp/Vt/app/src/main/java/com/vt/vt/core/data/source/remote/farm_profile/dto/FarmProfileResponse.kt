package com.vt.vt.core.data.source.remote.farm_profile.dto

import com.google.gson.annotations.SerializedName

data class FarmProfileResponse(

	@field:SerializedName("province")
	val province: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("address_one")
	val addressOne: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("address_two")
	val addressTwo: String? = null
)
