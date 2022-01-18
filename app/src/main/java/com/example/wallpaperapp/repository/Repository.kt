package com.example.wallpaperapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.wallpaperapp.data.api.PhotoApi
import com.example.wallpaperapp.data.paging.PagingDataSource
import com.example.wallpaperapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Repository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val photoApi: PhotoApi = retrofit.create(PhotoApi::class.java)

    fun getCuratedPhotos() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PagingDataSource(photoApi) }
    ).flow

    fun getPhotoByQuery(
        search_query: String
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PagingDataSource(photoApi, search_query) }
    ).flow

}