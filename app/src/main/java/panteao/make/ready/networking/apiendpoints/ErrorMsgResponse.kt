package me.vipa.app.networking

import com.google.gson.annotations.SerializedName

data class ErrorMsgResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("debugMessage")
	val debugMessage: String? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null
)