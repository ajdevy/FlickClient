package com.ajdev.flickrclient.photodetails.ui

import androidx.lifecycle.ViewModel
import com.ajdev.flickrclient.flickr.FlickrInteractor

class PhotoDetailsViewModel(
    val photoTitle: String,
    val photoUrl: String,
    private val flickrInteractor: FlickrInteractor
) : ViewModel() {

}