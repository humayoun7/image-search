package com.humayoun.imagesearch.data.paging

import androidx.paging.PagingSource
import com.humayoun.imagesearch.data.models.GalleryItem
import com.humayoun.imagesearch.data.models.getImagesOnly
import com.humayoun.imagesearch.data.serivce.ImgurService
import com.humayoun.imagesearch.Constants
import java.lang.Exception

class ImgurPagingSource (
    private val service: ImgurService,
    private val searchQuery: String
): PagingSource<Int, GalleryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        val page = params.key ?: Constants.ImgurService.STARTING_PAGE_INDEX

        return try {
            val resp = service.getImageData(searchQuery, page)
            val items = resp?.data.getImagesOnly()
            LoadResult.Page(
                data = items,
                prevKey = if (page == Constants.ImgurService.STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (exc: Exception) {
             LoadResult.Error(exc)
        }
    }
}