package com.vt.vt.core.data.source.remote.auth.model.profile

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("username")
    val username: String?
)