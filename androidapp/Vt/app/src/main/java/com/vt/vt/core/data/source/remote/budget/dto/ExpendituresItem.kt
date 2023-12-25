package com.vt.vt.core.data.source.remote.budget.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ExpendituresItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("budget_sub_category_id")
	val budgetSubCategoryId: Int? = null,

	@field:SerializedName("amount")
	val amount: BigDecimal? = null,

	@field:SerializedName("budget_category_id")
	val budgetCategoryId: Int? = null,

	@field:SerializedName("sku_id")
	val skuId: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)