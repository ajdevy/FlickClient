package com.ajdev.flickrclient.photodetails.ui

import androidx.lifecycle.ViewModel
import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.ajdev.flickrclient.photolist.ui.preload.FlickrModelLoader

class PhotoDetailsViewModel(
    val flickrPhoto: FlickrPhoto,
    private val flickrInteractor: FlickrInteractor
) : ViewModel() {

    val photoUrl: String
        get() = FlickrModelLoader.getLargestPhotoSizeUrl(flickrPhoto)

}