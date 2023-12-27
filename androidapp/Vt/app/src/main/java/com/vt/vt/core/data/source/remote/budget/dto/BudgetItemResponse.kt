package com.vt.vt.core.data.source.remote.budget.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BudgetItemResponse(

    @field:SerializedName("amount")
    val amount: BigDecimal? = null,

    @field:SerializedName("budget_category_id")
    val budgetCategoryId: Int? = null,

    @field:SerializedName("expenditures")
    val expenditures: List<ExpendituresItem>? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("left")
    val left: BigDecimal? = null,

    @field:SerializedName("budget_category_name")
    val budgetCategoryName: String? = null,

    @field:SerializedName("summary_text")
    val summaryText: String? = null
)