package com.humayoun.imagesearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.humayoun.imagesearch.data.paging.ImgurPagingSource
import com.humayoun.imagesearch.data.models.GalleryItem
import com.humayoun.imagesearch.data.serivce.ImgurService
import com.humayoun.imagesearch.Constants
import kotlinx.coroutines.flow.Flow

class ImageItemRepository (
    private val imgurService: ImgurService) {

    /**    Paging 3.0 :
    Handles in-memory cache.
    And, Requests data when the user is close to the end of the list. **/
    fun getSearchResultsStream(searchFor: String): Flow<PagingData<GalleryItem>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ImgurService.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                ImgurPagingSource(
                    imgurService,
                    searchFor
                )
            }
        ).flow
    }

}