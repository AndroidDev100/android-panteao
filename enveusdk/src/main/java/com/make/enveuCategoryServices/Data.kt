package com.watcho.enveu.bean

import com.google.gson.annotations.SerializedName

data class Data(

        @field:SerializedName("screen")
        val screen: String? = null,

        @field:SerializedName("widgets")
        val widgets: List<WidgetsItem?>? = null
)