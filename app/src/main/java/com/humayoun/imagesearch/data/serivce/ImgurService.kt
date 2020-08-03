package com.humayoun.imagesearch.data.serivce

import com.humayoun.imagesearch.data.models.ImugrResponse
import com.humayoun.imagesearch.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ImgurService {

    @GET(value = "/3/gallery/search/")
    suspend fun getImageData(
        @Query("q") searchFor: String,
        @Query("page") page: Int): ImugrResponse

    companion object {
        fun create(): ImgurService {
            val httpClinet = OkHttpClient.Builder()
                .addInterceptor(ImgurAuthInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.IMGUR_SERVICE.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClinet)
                .build()

            return retrofit.create(ImgurService::class.java)
        }
    }
}