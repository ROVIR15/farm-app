package com.vt.vt.core.data.source.remote.auth.model.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @field:SerializedName("username")
    val username: String?,

    @field:SerializedName("password")
    val password: String?,
)