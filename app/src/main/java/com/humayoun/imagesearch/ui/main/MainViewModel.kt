package com.humayoun.imagesearch.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.humayoun.imagesearch.data.models.ImageItem
import com.humayoun.imagesearch.data.repository.ImageItemRepository
import com.humayoun.imagesearch.data.serivce.ImgurService
import kotlinx.coroutines.flow.Flow

class MainViewModel(app: Application) : AndroidViewModel(app) {

    val imageRepository = ImageItemRepository(app, ImgurService.create())
    val imageData = imageRepository.imageData

    private var currentlySearchingFor: String? = null
    var currentSearchResults: Flow<PagingData<ImageItem>>? = null

    var selectedImage = MutableLiveData<ImageItem>()

    fun getImages(searchFor: String) {
        imageRepository.searchForImages(searchFor)
    }


    fun searchForImages(query: String): Flow<PagingData<ImageItem>> {
        val prevResults = currentSearchResults
        if(prevResults != null && query == currentlySearchingFor) {
            return prevResults
        }
        currentlySearchingFor = query
        val newResults = imageRepository.getSearchResultsStream(query)
        newResults.cachedIn(viewModelScope)
        currentSearchResults = newResults
        return newResults
    }

}
