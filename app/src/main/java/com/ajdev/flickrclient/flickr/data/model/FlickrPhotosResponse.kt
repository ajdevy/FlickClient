package com.ajdev.flickrclient.flickr.data.model

import com.adabadan.flickr.remote.model.FlickrPhotosPage
import com.google.gson.annotations.SerializedName

data class FlickrPhotosResponse (
        @SerializedName("photos") val photosPage: FlickrPhotosPage
)