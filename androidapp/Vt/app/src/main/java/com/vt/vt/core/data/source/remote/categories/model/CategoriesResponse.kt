package com.vt.vt.core.data.source.remote.categories.model

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

	@field:SerializedName("CategoriesResponse")
	val categoriesResponse: List<CategoriesResponseItem?>? = null
)

data class CategoriesResponseItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)
