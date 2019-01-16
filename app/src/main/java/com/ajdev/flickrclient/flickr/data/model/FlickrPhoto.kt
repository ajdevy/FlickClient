package com.ajdev.flickrclient.flickr.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FlickrPhoto(
    @SerializedName("id") val id: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String
) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}