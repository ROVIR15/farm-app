package com.vt.vt.core.data.source.remote.auth.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("token")
    val token: String?
)