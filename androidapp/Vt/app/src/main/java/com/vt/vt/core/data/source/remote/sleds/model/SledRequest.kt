package com.vt.vt.core.data.source.remote.sleds.model

import com.google.gson.annotations.SerializedName


data class SledRequest(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)
