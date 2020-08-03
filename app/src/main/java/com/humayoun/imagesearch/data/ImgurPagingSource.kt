package com.humayoun.imagesearch.data

import androidx.paging.PagingSource
import com.humayoun.imagesearch.data.models.ImageItem
import com.humayoun.imagesearch.data.models.getImagesOnly
import com.humayoun.imagesearch.data.serivce.ImgurService
import com.humayoun.imagesearch.utils.Constants
import java.lang.Exception

class ImgurPagingSource (
    private val service: ImgurService,
    private val searchQuery: String
): PagingSource<Int, ImageItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
        val page = params.key ?: Constants.IMGUR_SERVICE.STARTING_PAGE_INDEX

        return try {
            val resp = service.getImageData(searchQuery, page)
            val items = resp?.data.getImagesOnly()
            LoadResult.Page(
                data = items,
                prevKey = if (page == Constants.IMGUR_SERVICE.STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (exc: Exception) {
             LoadResult.Error(exc)
        }
    }
}