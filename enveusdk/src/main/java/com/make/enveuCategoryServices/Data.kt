package com.make.enveuCategoryServices

import com.google.gson.annotations.SerializedName
import com.make.enveuCategoryServices.WidgetsItem

data class Data(

        @field:SerializedName("screen")
        val screen: String? = null,

        @field:SerializedName("widgets")
        val widgets: List<WidgetsItem?>? = null
)