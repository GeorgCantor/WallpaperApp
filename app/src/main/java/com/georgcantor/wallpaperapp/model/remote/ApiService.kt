package com.georgcantor.wallpaperapp.model.remote

import com.georgcantor.wallpaperapp.model.remote.response.videos.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getVideos(
        @Url url: String,
        @Query("playlistId") playlistId: String
    ): Response<VideoResponse>
}