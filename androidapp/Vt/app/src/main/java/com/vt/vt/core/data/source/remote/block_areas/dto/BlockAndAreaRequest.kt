package com.vt.vt.core.data.source.remote.block_areas.dto

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class BlockAndAreaRequest(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,
)
