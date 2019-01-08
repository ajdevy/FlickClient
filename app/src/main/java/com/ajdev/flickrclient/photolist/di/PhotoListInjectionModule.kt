package com.ajdev.flickrclient.photolist.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ajdev.flickrclient.flickr.FlickrInteractor
import com.ajdev.flickrclient.photolist.ui.PhotoListViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

object PhotoListInjectionModule {

    val module = Kodein.Module(PhotoListInjectionModule.javaClass.name) {

        bind<PhotoListViewModel>() with factory { fragment: Fragment ->
            ViewModelProviders
                .of(
                    fragment,
                    PhotoListViewModelProviderFactory(instance())
                )
                .get(PhotoListViewModel::class.java)
        }
    }

    internal class PhotoListViewModelProviderFactory(
        private val flickrInteractor: FlickrInteractor
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PhotoListViewModel(flickrInteractor) as T
        }
    }
}