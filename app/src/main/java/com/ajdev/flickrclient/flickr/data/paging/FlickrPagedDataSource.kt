package com.ajdev.flickrclient.flickr.data.paging

import androidx.paging.PageKeyedDataSource
import com.ajdev.flickrclient.flickr.data.FlickrGateway
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.ajdev.flickrclient.flickr.data.model.FlickrPhotosResponse
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class FlickrPagedDataSource(
    private val flickrGateway: FlickrGateway,
    private val searchText: String
) : PageKeyedDataSource<Int, FlickrPhoto>() {

    private val compositeDisposable = CompositeDisposable()

    override fun invalidate() {
        super.invalidate()
        // dispose of all active subscriptions if the data source is invalidated
        compositeDisposable.dispose()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, FlickrPhoto>) {
        Timber.d("loadInitial placeholdersEnabled = ${params.placeholdersEnabled} loadSize = ${params.requestedLoadSize}")

        searchFlickr(FlickrGateway.FIRST_PAGE)
            .subscribe(
                {
                    Timber.d("loadInitial callback = $it")

                    callback.onResult(
                        it.photosPage.photos,
                        null,
                        FlickrGateway.FIRST_PAGE + 1
                    )
                },
                Timber::e
            )
            .addTo(compositeDisposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, FlickrPhoto>) {
        Timber.d("loadAfter key = ${params.key} loadSize = ${params.requestedLoadSize}")

        searchFlickr(params.key)
            .subscribe(
                {
                    Timber.d("loadAfter callback = $it")

                    callback.onResult(
                        it.photosPage.photos,
                        params.key + 1
                    )
                },
                Timber::e
            )
            .addTo(compositeDisposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, FlickrPhoto>) {
        Timber.d("loadBefore key = ${params.key} loadSize = ${params.requestedLoadSize}")
    }

    private fun searchFlickr(pageNumber: Int): Single<FlickrPhotosResponse> =
        if (searchText.isEmpty()) {
            // Parameterless searches have been disabled. Please use flickr.photos.getRecent instead
            flickrGateway.getRecent(pageNumber)
        } else {
            flickrGateway.search(pageNumber, searchText)
        }
}