package com.vt.vt.core.data.source.remote.budget.dto

import com.google.gson.annotations.SerializedName

data class BudgetBreakdownItem(

	@field:SerializedName("budget_category_id")
	val budgetCategoryId: Int? = null,

	@field:SerializedName("left")
	val left: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("total_expenditure")
	val totalExpenditure: Double? = null,

	@field:SerializedName("month_year")
	val monthYear: String? = null,

	@field:SerializedName("budget_amount")
	val budgetAmount: String? = null,

	@field:SerializedName("budget_category_name")
	val budgetCategoryName: String? = null
)