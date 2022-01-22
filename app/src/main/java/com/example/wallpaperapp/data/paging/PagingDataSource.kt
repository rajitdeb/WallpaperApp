package com.example.wallpaperapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.wallpaperapp.data.api.PhotoApi
import com.example.wallpaperapp.data.model.PhotoModel
import com.example.wallpaperapp.data.model.PhotoResponse
import com.example.wallpaperapp.utils.Constants
import com.example.wallpaperapp.utils.Constants.ORIENTATION_PORTRAIT
import retrofit2.Response
import java.lang.Exception

class PagingDataSource(
    private val photoApiService: PhotoApi,
    private val searchQuery: String? = null
) : PagingSource<Int, PhotoModel>() {

    override fun getRefreshKey(state: PagingState<Int, PhotoModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoModel> {
        return try {
            val position: Int = params.key ?: Constants.DEFAULT_PAGE_INDEX

            val response: Response<PhotoResponse> = if (searchQuery != null) {
                photoApiService.searchPhotoByQuery(
                    searchQuery,
                    position,
                    ORIENTATION_PORTRAIT,
                    Constants.RESULTS_PER_PAGE
                )
            } else {
                photoApiService.searchPhotoByDefaultQuery(
                    Constants.DEFAULT_QUERY,
                    position,
                    ORIENTATION_PORTRAIT,
                    Constants.RESULTS_PER_PAGE
                )
            }

            val photos = response.body()!!.photos
            Log.v("PagingDate", "Photo Model: ${response.body()?.photos}")

            LoadResult.Page(
                data = response.body()!!.photos,
                prevKey = if (position == Constants.DEFAULT_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}