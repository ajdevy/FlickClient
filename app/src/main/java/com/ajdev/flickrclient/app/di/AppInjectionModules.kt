package com.ajdev.flickrclient.app.di

import com.ajdev.flickrclient.flickr.di.FlickrInjectionModule
import com.ajdev.flickrclient.photodetails.di.PhotoDetailsInjectionModule
import com.ajdev.flickrclient.photolist.di.PhotoListInjectionModule
import org.kodein.di.Kodein

object AppInjectionModules {

    val module = Kodein.Module(AppInjectionModules.javaClass.name) {
        import(FlickrInjectionModule.module)
        import(PhotoListInjectionModule.module)
        import(PhotoDetailsInjectionModule.module)
    }
}