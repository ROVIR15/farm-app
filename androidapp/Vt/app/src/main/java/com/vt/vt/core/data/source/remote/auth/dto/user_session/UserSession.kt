package com.vt.vt.core.data.source.remote.auth.dto.user_session

data class UserSession(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)