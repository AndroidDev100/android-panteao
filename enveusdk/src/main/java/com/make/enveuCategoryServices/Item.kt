package com.watcho.enveu.bean

import com.make.enveuCategoryServices.AdUnitInfo
import com.make.enveuCategoryServices.LandingPage
import com.google.gson.annotations.SerializedName

data class Item(

        @field:SerializedName("showMoreButton")
        val showMoreButton: Boolean? = null,

        @field:SerializedName("playlist")
        val playlist: Playlist? = null,

        @field:SerializedName("listingLayout")
        val listingLayout: String? = null,

        @field:SerializedName("railCardType")
        val railCardType: String? = null,

        @field:SerializedName("railCardSize")
        val railCardSize: String? = null,

        @field:SerializedName("showHeader")
        val showHeader: Boolean? = null,

        @field:SerializedName("autoRotate")
        val autoRotate: Boolean? = null,

        @field:SerializedName("listingLayoutContentSize")
        val listingLayoutContentSize: Int? = null,

        @field:SerializedName("autoRotateDuration")
        val autoRotateDuration: Int? = null,

        @field:SerializedName("pageSize")
        val pageSize: Int? = null,

        @field:SerializedName("title")
        val title: Any? = null,

        @field:SerializedName("imageType")
        val imageType: String? = null,

        @field:SerializedName("moreViewConfig")
        val moreViewConfig: MoreViewConfig? = null,

        @field:SerializedName("contentIndicator")
        val contentIndicator: String? = null,

        @field:SerializedName("platform")
        val platform: String? = null,

        @field:SerializedName("item")
        val item: Item? = null,

        @field:SerializedName("imageSource")
        val imageSource: String? = null,

        @field:SerializedName("imageURL")
        val imageURL: String? = null,

        @field:SerializedName("assetId")
        val assetId: String? = null,

        @field:SerializedName("isProgram")
        val isProgram: Boolean? = null,

        @field:SerializedName("landingPage")
        val landingPage: LandingPage? = null,

        @field:SerializedName("adUnitInfo")
        val adUnitInfo: AdUnitInfo? = null

)