package com.ajdev.flickrclient.flickr.network;

import okhttp3.Interceptor
import okhttp3.Response

class FlickrApiKeyAdderInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder()
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("format", "json")
            .build()
        request = request.newBuilder().url(url).build();
        return chain.proceed(request)
    }
}