package com.vt.vt.core.data.source.remote.upload_image

import com.google.gson.annotations.SerializedName

data class PostFileResponse(

	@field:SerializedName("url_link")
	val urlLink: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
