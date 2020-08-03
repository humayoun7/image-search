package com.humayoun.imagesearch.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryItem (
    @Json(name = "id") val id: String?,
    @Json(name = "title") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "link") val link: String?,
    @Json(name = "type")val type: String?,
    @Json(name = "is_ad") val isAd: Boolean?,
    @Json(name = "is_album") val isAlbum: Boolean?,
    @Json(name = "images") val images: List<GalleryItem>?

) {
    val thumbnailUrl
        get() = "${link}m"

    val isImage
        get() = (type != null && type.contains("image")
                && isAd != null && !isAd)

}


// extension function to get the galleries only
fun List<GalleryItem>.getImagesOnly(): List<GalleryItem> {
    val imageList = arrayListOf<GalleryItem>()

    for (item  in this) {
        // item is image item, add in the list
        if (item.isAlbum != null && !item.isAlbum && item.isImage) {
            imageList.add(item)
        }

        // item is album, add galleries from album
        if(item.isAlbum != null && item.isAlbum && item.images != null) {
            for (img in item.images) {
                if(img.isImage) {
                    imageList.add(img)
                }
            }
        }
    }

    return imageList
}