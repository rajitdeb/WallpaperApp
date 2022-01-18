package com.example.wallpaperapp.data.api

import com.example.wallpaperapp.data.model.PhotoResponse
import com.example.wallpaperapp.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface PhotoApi {

    @Headers("Authorization: ${Constants.API_KEY}")
    @GET("curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<PhotoResponse>

    @Headers("Authorization: ${Constants.API_KEY}")
    @GET("search")
    suspend fun searchPhotoByQuery(
        @Query("query") searchQuery: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<PhotoResponse>

}