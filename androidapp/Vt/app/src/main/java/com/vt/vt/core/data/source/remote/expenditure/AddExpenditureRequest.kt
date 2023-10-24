package com.vt.vt.core.data.source.remote.expenditure

import com.google.gson.annotations.SerializedName

data class AddExpenditureRequest(

    @field:SerializedName("budget_category_id")
    val budgetCategoryId: Int,

    @field:SerializedName("budget_sub_category_id")
    val budgetSubCategoryId: Int,

    @field:SerializedName("amount")
    val amount: Double,

    @field:SerializedName("sku_id")
    val skuId: Int? = null,

    @field:SerializedName("remarks")
    val remarks: String? = null,

    @field:SerializedName("date")
    val date: String? = null
)
