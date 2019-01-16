package com.ajdev.flickrclient.flickr.data

import com.ajdev.flickrclient.flickr.data.model.FlickrPhotosResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrGateway {

    @GET("rest?method=flickr.photos.getRecent")
    fun getRecent(
        @Query("page") page: Int,
        @Query("safe_search") safeSearch: Int = 1
    ): Single<FlickrPhotosResponse>

    @GET("rest?method=flickr.photos.search")
    fun search(
        @Query("page") page: Int,
        @Query("text") query: String,
        @Query("safe_search") safeSearch: Int = 1
    ): Single<FlickrPhotosResponse>

    companion object {
        const val HOST_URL: String = "https://api.flickr.com/services/"
        const val FIRST_PAGE: Int = 1
    }
}