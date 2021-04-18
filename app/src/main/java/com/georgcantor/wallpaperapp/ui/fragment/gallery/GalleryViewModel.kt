package com.georgcantor.wallpaperapp.ui.fragment.gallery

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.georgcantor.wallpaperapp.model.remote.response.CommonPic
import com.georgcantor.wallpaperapp.repository.PicPagingSource
import com.georgcantor.wallpaperapp.repository.Repository
import kotlinx.coroutines.flow.Flow

class GalleryViewModel(private val repository: Repository) : ViewModel() {

    fun getPicListStream(query: String): Flow<PagingData<CommonPic>> {
        return Pager(PagingConfig(40)) {
            PicPagingSource(repository, query)
        }.flow
    }
}