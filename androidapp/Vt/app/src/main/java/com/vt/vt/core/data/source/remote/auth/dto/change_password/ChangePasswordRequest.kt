package com.vt.vt.core.data.source.remote.auth.dto.change_password

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("new_password")
	val newPassword: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
