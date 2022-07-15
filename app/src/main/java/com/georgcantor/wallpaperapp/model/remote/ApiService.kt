package com.georgcantor.wallpaperapp.model.remote

import com.georgcantor.wallpaperapp.BuildConfig
import com.georgcantor.wallpaperapp.model.remote.response.pexels.PhotoResponse
import com.georgcantor.wallpaperapp.model.remote.response.unsplash.UnsplashResponse
import com.georgcantor.wallpaperapp.model.remote.response.videos.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("search/photos/?client_id=${BuildConfig.UNSPLASH_CLIENT_ID}")
    suspend fun getUnsplashPictures(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<UnsplashResponse>

    @GET
    fun getPexelsPictures(
        @Url url: String,
        @Query("query") query: String,
        @Query("per_page") count: Int,
        @Query("page") page: Int
    ): Response<PhotoResponse>

    @GET
    suspend fun getVideos(
        @Url url: String,
        @Query("playlistId") playlistId: String
    ): Response<VideoResponse>
}