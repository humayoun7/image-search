package com.humayoun.imagesearch.data.serivce

import com.humayoun.imagesearch.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class ImgurAuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(Constants.IMGUR_SERVICE.AUTHORIZATION_HEADER, Constants.IMGUR_SERVICE.AUTHORIZATION_HEADER_VALUE)
            .build()

        return  chain.proceed(request)
    }
}