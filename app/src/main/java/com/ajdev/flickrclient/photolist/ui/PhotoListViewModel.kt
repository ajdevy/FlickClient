package com.ajdev.flickrclient.photolist.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

class PhotoListViewModel(flickrInteractor: FlickrInteractor) : ViewModel() {

    val recentPhotos: Flowable<PagedList<FlickrPhoto>> =
        flickrInteractor
            .getRecentPhotos(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )
            .replay(1)
            .autoConnect(0, { compositeDisposable::add })

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val PAGE_SIZE = 100
        private const val PREFETCH_DISTANCE = 100
    }
}
