package com.ajdev.flickrclient.photodetails.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.photodetails.ui.PhotoDetailsViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

object PhotoDetailsInjectionModule {

    val module = Kodein.Module(PhotoDetailsInjectionModule.javaClass.name) {

        bind<PhotoDetailsViewModel>() with factory { fragment: Fragment, photoTitle: String, photoUrl: String ->
            ViewModelProviders
                .of(
                    fragment,
                    PhotoDetailsViewModelProviderFactory(instance(), photoTitle, photoUrl)
                )
                .get(PhotoDetailsViewModel::class.java)
        }
    }

    internal class PhotoDetailsViewModelProviderFactory(
        private val flickrInteractor: FlickrInteractor,
        private val photoTitle: String,
        private val photoUrl: String
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PhotoDetailsViewModel(photoTitle, photoUrl, flickrInteractor) as T
        }
    }
}