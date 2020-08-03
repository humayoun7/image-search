package com.humayoun.imagesearch.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImugrResponse (
    val data: List<GalleryItem>,
    val success: Boolean,
    val status: Int
)