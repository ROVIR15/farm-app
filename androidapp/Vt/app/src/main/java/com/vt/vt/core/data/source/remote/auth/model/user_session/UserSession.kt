package com.vt.vt.core.data.source.remote.auth.model.user_session

data class UserSession(
    val username: String,
    val password: String,
    val isLogin: Boolean = false
)