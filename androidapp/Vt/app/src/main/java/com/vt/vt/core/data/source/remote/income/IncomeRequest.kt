package com.vt.vt.core.data.source.remote.income

import com.google.gson.annotations.SerializedName

data class IncomeRequest(

    @field:SerializedName("date") val date: String,

    @field:SerializedName("income_category_id") val incomeCategoryId: Int? = null,

    @field:SerializedName("amount") val amount: Double,

    @field:SerializedName("remarks") val remarks: String? = null
)
