package com.vt.vt.core.data.source.remote.income.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class IncomeRequest(

    @field:SerializedName("date") val date: String,

    @field:SerializedName("income_category_id") val incomeCategoryId: Int? = null,

    @field:SerializedName("amount") val amount: BigDecimal,

    @field:SerializedName("remarks") val remarks: String? = null
)
