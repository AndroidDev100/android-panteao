package com.watcho.enveu.bean

import com.google.gson.annotations.SerializedName

data class EnveuCategory(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("responseCode")
        val responseCode: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)