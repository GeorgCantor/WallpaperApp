package com.georgcantor.wallpaperapp.repository

import android.content.Context
import com.georgcantor.wallpaperapp.BuildConfig.YOUTUBE_URL
import com.georgcantor.wallpaperapp.R
import com.georgcantor.wallpaperapp.model.local.FavDao
import com.georgcantor.wallpaperapp.model.local.Favorite
import com.georgcantor.wallpaperapp.model.remote.ApiService
import com.georgcantor.wallpaperapp.model.remote.response.*
import com.georgcantor.wallpaperapp.util.Constants.VIDEOS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: ApiService,
    private val dao: FavDao
) {

    init {
        NETWORK_ERROR_TITLE = context.getString(R.string.error_internet_title)
        NETWORK_ERROR_MESSAGE = context.getString(R.string.error_internet_message)
        NETWORK_ERROR_BUTTON = context.getString(R.string.error_internet_button)
        DEFAULT_ERROR_TITLE = context.getString(R.string.error_something_wrong_title)
        DEFAULT_ERROR_MESSAGE = context.getString(R.string.error_something_wrong_message)
        DEFAULT_ERROR_BUTTON = context.getString(R.string.error_internet_button)
    }

    suspend fun getPics(query: String, page: Int): List<CommonPic> {
        val list = mutableListOf<CommonPic>()

        withContext(Dispatchers.IO) {
            val respUnsplash = service.getUnsplashPictures(query, page)
            val listUnsplash = respUnsplash.body()?.results?.map {
                CommonPic(
                    url = it.urls?.small.orEmpty(),
                    width = it.width ?: 0,
                    height = it.height ?: 0,
                    tags = it.altDescription,
                    imageURL = it.urls?.full,
                    fullHDURL = it.urls?.regular,
                    id = it.hashCode(),
                    videoId = ""
                )
            }
            listUnsplash?.let { list.addAll(it) }
        }

//        withContext(Dispatchers.IO) {
//            val response = service.getPexelsPictures(PEXELS_URL, query, 15, page)
//            val listPexels = response.body()?.photos?.map {
//                CommonPic(
//                    url = it.src?.small.orEmpty(),
//                    width = it.width,
//                    height = it.height,
//                    tags = "",
//                    imageURL = it.src?.original.orEmpty(),
//                    fullHDURL = it.src?.large.orEmpty(),
//                    id = it.hashCode(),
//                    videoId = it.id.toString()
//                )
//            }
//            listPexels?.let { list.addAll(it) }
//        }

        return list
    }

    suspend fun getVideos() = service.getVideos(YOUTUBE_URL, VIDEOS)

    suspend fun addToFavorites(favorite: Favorite) = dao.insert(favorite)

    suspend fun removeFromFavorites(url: String?) = dao.deleteByUrl(url)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun getByUrl(url: String) = dao.getByUrl(url)

    suspend fun getFavorites() = dao.getAll()

    suspend fun isFavorite(url: String?) = dao.isFavorite(url)
}