package com.vt.vt.core.data.source.remote.expenditure

import com.google.gson.annotations.SerializedName

data class ExpenditureResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null,
)
