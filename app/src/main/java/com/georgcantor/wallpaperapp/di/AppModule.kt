package com.georgcantor.wallpaperapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.georgcantor.wallpaperapp.model.remote.ApiClient
import com.georgcantor.wallpaperapp.util.Constants.MAIN_STORAGE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesApiClient(@ApplicationContext context: Context) = ApiClient.create(context)

    @Singleton
    @Provides
    fun providesPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences(MAIN_STORAGE, MODE_PRIVATE)
}