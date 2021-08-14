package com.make.enveuCategoryServices

import com.google.gson.annotations.SerializedName

data class LandingPage (

    @field:SerializedName("landingPageTitle")
        val landingPageTitle: String? = null,

    @field:SerializedName("assetId")
        val assetID: String? = null,

    @field:SerializedName("isProgram")
        val isProgram: Boolean? = null,

    @field:SerializedName("link")
        val link: String? = null,

    @field:SerializedName("type")
        val type: String? = null,

    @field:SerializedName("playlist")
        val playlist: Playlist? = null,

    @field:SerializedName("target")
        val target: String? = null

)
