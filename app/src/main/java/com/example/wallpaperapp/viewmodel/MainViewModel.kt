package com.example.wallpaperapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.wallpaperapp.data.model.PhotoModel
import com.example.wallpaperapp.data.model.PhotoResponse
import com.example.wallpaperapp.data.paging.PagingDataSource
import com.example.wallpaperapp.repository.Repository
import com.example.wallpaperapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository: Repository = Repository()

    val photos = repository.getDefaultPhotosList().cachedIn(viewModelScope)

    fun searchPhotos(query: String): Flow<PagingData<PhotoModel>> {
        return repository.getPhotoByQuery(query).cachedIn(viewModelScope)
    }
}