package com.humayoun.imagesearch.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.humayoun.imagesearch.data.models.GalleryItem
import com.humayoun.imagesearch.data.repository.ImageItemRepository
import com.humayoun.imagesearch.data.serivce.ImgurService
import kotlinx.coroutines.flow.Flow

class MainViewModel: ViewModel() {

    private val imageRepository = ImageItemRepository(ImgurService.create())

    var currentlySearchingFor: String? = null
    var currentSearchResults: Flow<PagingData<GalleryItem>>? = null
    var selectedImage = MutableLiveData<GalleryItem>()

    fun searchForImages(query: String): Flow<PagingData<GalleryItem>> {
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
