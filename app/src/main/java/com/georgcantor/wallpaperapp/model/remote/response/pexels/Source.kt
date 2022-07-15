package com.georgcantor.wallpaperapp.model.remote.response.pexels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Source {

    @SerializedName("original")
    @Expose
    val original: String? = null

    @SerializedName("large2x")
    @Expose
    val large2x: String? = null

    @SerializedName("large")
    @Expose
    val large: String? = null

    @SerializedName("medium")
    @Expose
    val medium: String? = null

    @SerializedName("small")
    @Expose
    val small: String? = null

    @SerializedName("portrait")
    @Expose
    val portrait: String? = null

    @SerializedName("landscape")
    @Expose
    val landscape: String? = null

    @SerializedName("tiny")
    @Expose
    val tiny: String? = null
}
