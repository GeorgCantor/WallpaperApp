package com.georgcantor.wallpaperapp.model.remote.response.pexels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Photo {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("width")
    @Expose
    var width: Int = 0

    @SerializedName("height")
    @Expose
    var height: Int = 0

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("photographer")
    @Expose
    var photographer: String? = null

    @SerializedName("photographer_url")
    @Expose
    var photographerUrl: String? = null

    @SerializedName("photographer_id")
    @Expose
    var photographerId: Int = 0

    @SerializedName("src")
    @Expose
    var src: Source? = null
}
