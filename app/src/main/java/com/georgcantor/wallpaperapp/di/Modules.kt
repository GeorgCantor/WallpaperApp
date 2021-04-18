package com.georgcantor.wallpaperapp.di

import android.content.Context.MODE_PRIVATE
import com.georgcantor.wallpaperapp.model.local.FavDatabase
import com.georgcantor.wallpaperapp.model.remote.ApiClient
import com.georgcantor.wallpaperapp.repository.Repository
import com.georgcantor.wallpaperapp.ui.activity.categories.CategoriesViewModel
import com.georgcantor.wallpaperapp.ui.activity.detail.DetailViewModel
import com.georgcantor.wallpaperapp.ui.fragment.favorites.FavoritesViewModel
import com.georgcantor.wallpaperapp.ui.activity.main.MainViewModel
import com.georgcantor.wallpaperapp.ui.activity.videos.VideosViewModel
import com.georgcantor.wallpaperapp.ui.fragment.gallery.GalleryViewModel
import com.georgcantor.wallpaperapp.util.Constants.MAIN_STORAGE
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module { single { ApiClient.create(get()) } }

val dbModule = module { single { FavDatabase.buildDefault(get()).dao() } }

val repositoryModule = module { single { Repository(get(), get()) } }

val preferenceModule = module {
    single { androidApplication().getSharedPreferences(MAIN_STORAGE, MODE_PRIVATE) }
}

val viewModelModule = module(override = true) {
    viewModel { MainViewModel(get()) }
    viewModel { GalleryViewModel(get()) }
    viewModel { CategoriesViewModel(androidApplication(), get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { VideosViewModel(get()) }
}