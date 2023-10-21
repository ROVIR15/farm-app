package com.vt.vt.core.data.source.remote.budget

import com.google.gson.annotations.SerializedName

data class IncomesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("income_category_id")
	val incomeCategoryId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("remarks")
	val remarks: Any? = null
)