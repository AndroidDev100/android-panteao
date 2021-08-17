package com.make.enveuCategoryServices

import com.google.gson.annotations.SerializedName

data class MoreViewConfig(

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("filters")
        val filters: List<Any?>? = null,

        @field:SerializedName("sortable")
        val sortable: Boolean? = null
)