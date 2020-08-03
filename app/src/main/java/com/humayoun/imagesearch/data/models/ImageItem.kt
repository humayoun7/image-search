package com.humayoun.imagesearch.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageItem (
    @Json(name = "id") val id: String?,
    @Json(name = "title") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "link") val link: String?,
    @Json(name = "type")val type: String?,
    @Json(name = "is_ad") val isAd: Boolean?,
    @Json(name = "is_album") val isAlbum: Boolean?,
    @Json(name = "images") val images: List<ImageItem>?

) {
    val thumbnailUrl
        get() = "${link}m"

    val isImage
        get() = (type != null && type.contains("image"))

}


// extension function to get the images only
fun List<ImageItem>.getImagesOnly(): List<ImageItem> {
    val imageList = arrayListOf<ImageItem>()

    for (item  in this) {
        // item is image item, add in the list
        if (item.isAlbum != null && !item.isAlbum && item.isImage) {
            imageList.add(item)
        }

        // item is album, add images from album
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