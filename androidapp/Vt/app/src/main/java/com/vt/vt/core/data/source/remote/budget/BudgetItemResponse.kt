package com.vt.vt.core.data.source.remote.budget

import com.google.gson.annotations.SerializedName

data class BudgetItemResponse(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("budget_category_id")
	val budgetCategoryId: Int? = null,

	@field:SerializedName("expenditures")
	val expenditures: List<ExpendituresItem>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("budget_category_name")
	val budgetCategoryName: String? = null
)