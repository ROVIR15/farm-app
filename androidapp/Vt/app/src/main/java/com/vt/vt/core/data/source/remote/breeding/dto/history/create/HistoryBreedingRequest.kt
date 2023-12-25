package com.vt.vt.core.data.source.remote.breeding.dto.history.create

import com.google.gson.annotations.SerializedName

data class HistoryBreedingRequest(

	@field:SerializedName("birth_date")
	val birthDate: String,

	@field:SerializedName("breeding_id")
	val breedingId: Int,

	@field:SerializedName("remarks")
	val remarks: String
)
