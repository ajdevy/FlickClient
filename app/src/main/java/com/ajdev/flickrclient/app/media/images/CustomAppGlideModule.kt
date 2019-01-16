package com.ajdev.flickrclient.app.media.images

import android.content.Context
import androidx.annotation.NonNull
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.ajdev.flickrclient.photolist.ui.preload.FlickrModelLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream


@GlideModule
class CustomAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(4f)
            .build()
        builder.setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
    }

    override fun registerComponents(
        @NonNull context: Context, @NonNull glide: Glide,
        @NonNull registry: Registry
    ) {
        registry.append(FlickrPhoto::class.java, InputStream::class.java, FlickrModelLoader.Factory())
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}