package com.ajdev.flickrclient.flickr

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.ajdev.flickrclient.flickr.data.FlickrGateway
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.ajdev.flickrclient.flickr.data.paging.FlickrPagedDataSourceFactory
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FlickrInteractor(private val flickrGateway: FlickrGateway) {

    fun getRecentPhotos(pageSize: Int, prefetchDistance: Int): Flowable<PagedList<FlickrPhoto>> {

        val flickrPagedDataSourceFactory = FlickrPagedDataSourceFactory(flickrGateway, "")

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setPrefetchDistance(prefetchDistance)
            .setEnablePlaceholders(false)
            .build()

        return RxPagedListBuilder<Int, FlickrPhoto>(flickrPagedDataSourceFactory, config)
            .setFetchScheduler(Schedulers.io())
            .setNotifyScheduler(AndroidSchedulers.mainThread())
            // get only the latest paged list if we have backpressure
            .buildFlowable(BackpressureStrategy.LATEST)
    }
}