package com.georgcantor.wallpaperapp.network;

import com.georgcantor.wallpaperapp.BuildConfig;
import com.georgcantor.wallpaperapp.model.Pic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("?key=" + BuildConfig.API_KEY + "&order=popular")
    Call<Pic> getPopularPic(@Query("page") int index);

    @GET("?key=" + BuildConfig.API_KEY)
    Call<Pic> getCatPic(@Query("category") String category,
                        @Query("page") int index);

    @GET("?key=" + BuildConfig.API_KEY + "&order=latest")
    Call<Pic> getLatestPic(@Query("page") int index);

    @GET("?key=" + BuildConfig.API_KEY + "&editors_choice=true")
    Call<Pic> getEditorPic(@Query("page") int index);
}
