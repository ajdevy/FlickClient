package com.ajdev.flickrclient.flickr.data.paging

import androidx.paging.DataSource
import com.ajdev.flickrclient.flickr.data.FlickrGateway
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto

class FlickrPagedDataSourceFactory(
    private val flickrGateway: FlickrGateway,
    private val searchText: String
) : DataSource.Factory<Int, FlickrPhoto>() {

    override fun create(): DataSource<Int, FlickrPhoto> = FlickrPagedDataSource(flickrGateway, searchText)
}