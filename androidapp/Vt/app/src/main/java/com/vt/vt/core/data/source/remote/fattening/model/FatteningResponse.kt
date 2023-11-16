package com.vt.vt.core.data.source.remote.fattening.model

import com.google.gson.annotations.SerializedName

data class FatteningResponse(

	@field:SerializedName("weight_results")
	val weightResults: WeightResults? = null,

	@field:SerializedName("bcs_results")
	val bcsResults: BcsResults? = null
)

data class WeightResults(

	@field:SerializedName("date")
	val date: List<String>? = null,

	@field:SerializedName("score")
	val score: List<String>? = null,

	@field:SerializedName("label")
	val label: String? = null
)

data class BcsResults(

	@field:SerializedName("date")
	val date: List<String>? = null,

	@field:SerializedName("score")
	val score: List<String>? = null,

	@field:SerializedName("label")
	val label: String? = null
)
