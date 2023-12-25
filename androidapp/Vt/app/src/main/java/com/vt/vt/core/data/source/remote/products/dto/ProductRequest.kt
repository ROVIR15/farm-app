package com.vt.vt.core.data.source.remote.products.dto

import com.google.gson.annotations.SerializedName

data class ProductRequest(

	@field:SerializedName("category_id")
	val categoryId: Int,

	@field:SerializedName("unit_measurement")
	val unitMeasurement: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String
)
