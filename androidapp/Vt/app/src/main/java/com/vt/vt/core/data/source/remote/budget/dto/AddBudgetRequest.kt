package com.vt.vt.core.data.source.remote.budget.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class AddBudgetRequest(

	@field:SerializedName("budget_category_id")
	val budgetCategoryId: Int,

	@field:SerializedName("amount")
	val amount: BigDecimal,

	@field:SerializedName("month_year")
	val monthYear: String
)

