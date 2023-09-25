package com.vt.vt.core.data.source.remote.products.model

import com.google.gson.annotations.SerializedName

data class ProductRequest(

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("unit_measurement")
	val unitMeasurement: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null
)
