package com.humayoun.imagesearch.data.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.humayoun.imagesearch.R
import com.humayoun.imagesearch.data.ImgurPagingSource
import com.humayoun.imagesearch.data.models.ImageItem
import com.humayoun.imagesearch.data.models.ImugrResponse
import com.humayoun.imagesearch.data.models.getImagesOnly
import com.humayoun.imagesearch.data.serivce.ImgurService
import com.humayoun.imagesearch.utils.Constants
import com.humayoun.imagesearch.utils.FileHelper
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ImageItemRepository (
    private val imgurService: ImgurService) {

    /**    Paging 3.0 :
    Handles in-memory cache.
    And, Requests data when the user is close to the end of the list. **/
    fun getSearchResultsStream(searchFor: String): Flow<PagingData<ImageItem>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.IMGUR_SERVICE.PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ImgurPagingSource(imgurService, searchFor)}
        ).flow
    }

}