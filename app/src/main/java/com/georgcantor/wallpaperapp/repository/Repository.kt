package com.georgcantor.wallpaperapp.repository

import com.georgcantor.wallpaperapp.BuildConfig.YOUTUBE_URL
import com.georgcantor.wallpaperapp.model.remote.ApiService
import com.georgcantor.wallpaperapp.util.Constants.VIDEOS
import javax.inject.Inject

class Repository @Inject constructor(private val service: ApiService) {

    suspend fun getVideos() = service.getVideos(YOUTUBE_URL, VIDEOS)
}