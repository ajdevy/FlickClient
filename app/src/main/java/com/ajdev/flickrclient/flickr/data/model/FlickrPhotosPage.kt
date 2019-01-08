package com.adabadan.flickr.remote.model

import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.google.gson.annotations.SerializedName

data class FlickrPhotosPage(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perpage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photos: List<FlickrPhoto>
)
