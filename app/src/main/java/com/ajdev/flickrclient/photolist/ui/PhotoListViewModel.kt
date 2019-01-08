package com.ajdev.flickrclient.photolist.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import io.reactivex.Flowable

class PhotoListViewModel(private val flickrInteractor: FlickrInteractor) : ViewModel() {

    fun getRecentPhotos(): Flowable<PagedList<FlickrPhoto>> =
        flickrInteractor.getRecentPhotos(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE)

    companion object {
        private const val PAGE_SIZE = 40
        private const val PREFETCH_DISTANCE = 40
    }
}
