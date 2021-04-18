package com.georgcantor.wallpaperapp.ui.fragment.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgcantor.wallpaperapp.model.local.Favorite
import com.georgcantor.wallpaperapp.repository.Repository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: Repository) : ViewModel() {

    val favorites = MutableLiveData<List<Favorite>>()

    fun getFavorites() {
        viewModelScope.launch {
            val favs = repository.getFavorites()
            favorites.postValue(favs)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
            getFavorites()
        }
    }
}