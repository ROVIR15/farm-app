package com.vt.vt.core.data.source.remote.income.dto

import com.google.gson.annotations.SerializedName

data class IncomeResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)