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
    private val app: Application,
    private val imgurService: ImgurService) {

    val imageData = MutableLiveData<List<ImageItem>>()



    fun getImages() {
        val text = FileHelper.getTextFromResources(app, R.raw.imugr_sample_data)
        Log.i("imugr", text)

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ImugrResponse::class.java)
        val resp = adapter.fromJson(text)

        imageData.value = resp?.data ?: emptyList()
    }

    private fun networkAvailable (): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false

    }

     fun searchForImages(searchFor: String) {
        CoroutineScope(Dispatchers.IO).launch {
            callSearchGallery(searchFor)
        }
    }

    @WorkerThread
    suspend fun callSearchGallery(searchFor: String) {
        if(networkAvailable()) {
            val respData = imgurService.getImageData(searchFor,1)
            val imageItems = respData?.data?.getImagesOnly() ?: emptyList()
            imageData.postValue(imageItems)
        }
    }


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