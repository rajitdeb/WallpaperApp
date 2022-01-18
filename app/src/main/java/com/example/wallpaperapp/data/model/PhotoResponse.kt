package com.example.wallpaperapp.data.model

data class PhotoResponse(
    val prev_page: String,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoModel>,
    val total_results: Int
)