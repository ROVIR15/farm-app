package com.vt.vt.core.data.source.remote.profile.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("username")
    val username: String?
)