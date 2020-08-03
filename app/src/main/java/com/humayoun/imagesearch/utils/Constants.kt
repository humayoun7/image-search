package com.humayoun.imagesearch.utils

object Constants {
    object ImgurService {
        const val BASE_URL = "https://api.imgur.com/"
        const val AUTHORIZATION_HEADER = "Authorization"
        const val AUTHORIZATION_HEADER_VALUE = "Client-ID b067d5cb828ec5a"
        const val STARTING_PAGE_INDEX = 1
        const val PAGE_SIZE = 50
    }

    // to show some galleries when the app starts
    const val INITIAL_QUERY = "random"
}