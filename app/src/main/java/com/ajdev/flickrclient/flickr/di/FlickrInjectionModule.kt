package com.ajdev.flickrclient.flickr.di

import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.flickr.data.FlickrGateway
import com.ajdev.flickrclient.flickr.data.paging.FlickrPagedDataSource
import com.ajdev.flickrclient.flickr.network.FlickrApiKeyAdderInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlickrInjectionModule {

    private const val FLICKR_API = "1879e2c12dc9064e3e7d630973aded66"

    val module = Kodein.Module(javaClass.name) {

        bind<FlickrInteractor>() with provider {
            val flickrGateway = instance<FlickrGateway>()
            FlickrInteractor(flickrGateway)
        }

        bind<FlickrGateway>() with provider {

            val okHttpClient = instance<OkHttpClient>()

            val retrofit = Retrofit.Builder()
                .baseUrl(FlickrGateway.HOST_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(FlickrGateway::class.java)
        }

        bind<OkHttpClient>() with provider {

            val httpLoggingInterceptor = HttpLoggingInterceptor()

            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            OkHttpClient.Builder()
                .addInterceptor(FlickrApiKeyAdderInterceptor(FLICKR_API))
                .addInterceptor(httpLoggingInterceptor)
                .build()
        }
    }
}