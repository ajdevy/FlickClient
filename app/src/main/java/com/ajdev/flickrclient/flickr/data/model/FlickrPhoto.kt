package com.ajdev.flickrclient.flickr.data.model

import com.google.gson.annotations.SerializedName

data class FlickrPhoto(
    @SerializedName("id") val id: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String
) {

    val smallPhotoUrl: String =
        "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg"

    val largePhotoUrl: String
        get() = "https://farm$farm.staticflickr.com/$server/${id}_${secret}_b.jpg"
}