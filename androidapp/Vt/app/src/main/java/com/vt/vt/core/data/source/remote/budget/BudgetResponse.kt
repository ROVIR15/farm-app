package com.vt.vt.core.data.source.remote.budget

import com.google.gson.annotations.SerializedName

data class BudgetResponse(

	@field:SerializedName("budget_breakdown")
	val budgetBreakdown: List<BudgetBreakdownItem>? = null,

	@field:SerializedName("incomes")
	val incomes: List<IncomesItem>? = null,

	@field:SerializedName("budget_left")
	val budgetLeft: String? = null,

	@field:SerializedName("total_budget_amount")
	val totalBudgetAmount: String? = null,

	@field:SerializedName("total_expenditure")
	val totalExpenditure: Double? = null,

	@field:SerializedName("month_year")
	val monthYear: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)