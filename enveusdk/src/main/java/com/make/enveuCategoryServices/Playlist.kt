package com.make.enveuCategoryServices

import com.google.gson.annotations.SerializedName

data class Playlist(

        @field:SerializedName("playlistId")
        val playlistId: String? = null,

        @field:SerializedName("kalturaChannelId")
        val kalturaChannelId: String? = null,

        @field:SerializedName("brightcovePlaylistId")
        val brightcovePlaylistId: String? = null,

        @field:SerializedName("kEntryId")
        val kEntryId: String? = null,

        @field:SerializedName("predefPlaylistType")
        val predefPlaylistType: String? = null,

        @field:SerializedName("forLoggedInUser")
        val forLoggedInUser: Any? = null,

        @field:SerializedName("forAnonymousUser")
        val forAnonymousUser: Any? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("playlistName")
        val playlistName: String? = null,

        @field:SerializedName("tags")
        val tags: List<String?>? = null,

        @field:SerializedName("referenceName")
        val referenceName: String? = null,

        @field:SerializedName("status")
        val status: String? = null
)