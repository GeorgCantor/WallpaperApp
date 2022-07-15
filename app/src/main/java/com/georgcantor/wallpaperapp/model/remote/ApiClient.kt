package com.georgcantor.wallpaperapp.model.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.georgcantor.wallpaperapp.BuildConfig
import com.georgcantor.wallpaperapp.BuildConfig.DEBUG
import com.georgcantor.wallpaperapp.util.isNetworkAvailable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {

    fun create(context: Context): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (DEBUG) Level.BODY else Level.NONE

        val okHttpClient = OkHttpClient().newBuilder().apply {
            if (DEBUG) addInterceptor(ChuckerInterceptor(context))
        }
            .addInterceptor(CacheInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor())
            .cache(Cache(File(context.cacheDir, "ResponsesCache"), (30 * 1024 * 1024).toLong()))
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    class CacheInterceptor(private val context: Context) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()

            if (!context.isNetworkAvailable()) {
                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 2419200)
                    .build()
            }

            return chain.proceed(request)
        }
    }
}