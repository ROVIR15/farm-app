package com.vt.vt.core.data.source.remote.budget

import com.google.gson.annotations.SerializedName

data class AddBudgetRequest(

	@field:SerializedName("budget_category_id")
	val budgetCategoryId: Int,

	@field:SerializedName("amount")
	val amount: Double,

	@field:SerializedName("month_year")
	val monthYear: String
)

