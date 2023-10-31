package com.vt.vt.core.data.source.remote.income

import com.google.gson.annotations.SerializedName

data class IncomeCategoriesResponse(

	@field:SerializedName("IncomeCategoriesResponse")
	val incomeCategoriesResponse: List<IncomeCategoriesResponseItem>
)

data class IncomeCategoriesResponseItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
