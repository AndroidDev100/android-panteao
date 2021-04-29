package com.watcho.enveu.bean

import com.google.gson.annotations.SerializedName

data class WidgetsItem(

        @field:SerializedName("layout")
        val layout: String? = null,

        @field:SerializedName("item")
        val item: Item? = null,

        @field:SerializedName("appScreens")
        val appScreens: Any? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("displayOrder")
        val displayOrder: Int? = null,

        @field:SerializedName("height")
        val height: Any? = null,

        @field:SerializedName("width")
        val width: Any? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("containerId")
        val containerId: String? = null,

        @field:SerializedName("status")
        val status: String? = null,

        @field:SerializedName("brightcoveImageType")
         val widgetImageType: String? = null
)