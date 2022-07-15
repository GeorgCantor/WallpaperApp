package com.georgcantor.wallpaperapp.model.remote.response.pexels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhotoResponse {

    @SerializedName("page")
    @Expose
    private val page: Int = 0

    @SerializedName("per_page")
    @Expose
    private val perPage: Int = 0

    @SerializedName("photos")
    @Expose
    val photos: List<Photo>? = null

    @SerializedName("next_page")
    @Expose
    private val nextPage: String? = null
}