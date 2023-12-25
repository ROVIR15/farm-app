package com.vt.vt.core.data.source.remote.breeding.dto.pregnancy

import com.google.gson.annotations.SerializedName

data class PregnancyRequest(

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("is_active")
	val isActive: Boolean,

	@field:SerializedName("remarks")
	val remarks: String? = null
)
