package com.ajdev.flickrclient.photolist.ui.preload

import android.util.SparseArray
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.bumptech.glide.util.LruCache
import timber.log.Timber
import java.io.InputStream
import java.util.*

/**
 * An implementation of ModelStreamLoader that leverages the StreamOpener class and the
 * ExecutorService backing the Engine to download the image and resize it in memory before saving
 * the resized version directly to the disk cache.
 */
class FlickrModelLoader private constructor(
    urlLoader: ModelLoader<GlideUrl, InputStream>, modelCache: ModelCache<FlickrPhoto, GlideUrl>
) : BaseGlideUrlLoader<FlickrPhoto>(urlLoader, modelCache) {

    override fun handles(model: FlickrPhoto): Boolean {
        return true
    }

    override fun getUrl(model: FlickrPhoto, width: Int, height: Int, options: Options): String {
        return getPhotoUrl(model, width, height)
    }

    override fun getAlternateUrls(photo: FlickrPhoto, width: Int, height: Int, options: Options?): List<String> {
        return getAlternateUrls(photo, width, height)
    }

    /**
     * The default factory for [FlickrModelLoader]s.
     */
    class Factory : ModelLoaderFactory<FlickrPhoto, InputStream> {
        private val modelCache = ModelCache<FlickrPhoto, GlideUrl>(500)

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<FlickrPhoto, InputStream> {
            return FlickrModelLoader(
                multiFactory.build(GlideUrl::class.java, InputStream::class.java),
                modelCache
            )
        }

        override fun teardown() {
            modelCache.clear()
        }
    }

    companion object {

        private const val MAX_URLS_TO_CACHE = 2000

        private const val CACHEABLE_PHOTO_URL = "https://farm%s.staticflickr.com/%s/%s_%s_"

        private val EDGE_TO_SIZE_KEY = object : SparseArray<String>() {
            init {
                put(75, "s")
                put(100, "t")
                put(150, "q")
                put(240, "m")
                put(320, "n")
                put(640, "z")
                put(1024, "b")
            }
        }

        private val CACHED_URLS = LruCache<UrlCacheKey, String>(MAX_URLS_TO_CACHE.toLong())

        private val SORTED_SIZE_KEYS = ArrayList<Int>(EDGE_TO_SIZE_KEY.size())
            .apply {
                for (i in 0 until EDGE_TO_SIZE_KEY.size()) {
                    add(EDGE_TO_SIZE_KEY.keyAt(i))
                }
                sort()
            }

        val SQUARE_THUMB_SIZE_KEY: Int = SORTED_SIZE_KEYS.first()
        val LARGEST_SIZE_KEY: Int = SORTED_SIZE_KEYS.last()
        val LARGEST_SIZE_VALUE: String = EDGE_TO_SIZE_KEY[LARGEST_SIZE_KEY]
        val Z_SIZE_VALUE: Int = 640

        fun getLargestPhotoSizeUrl(photo: FlickrPhoto): String {
            val entry = UrlCacheKey(photo, LARGEST_SIZE_VALUE)
            var result = CACHED_URLS.get(entry)
            if (result == null) {
                result = getPartialUrl(photo) + LARGEST_SIZE_VALUE + ".jpg"
                CACHED_URLS.put(entry, result)
            }
            return result
        }

        private fun getSizeKey(width: Int, height: Int): String {
            val largestEdge = Math.max(width, height)

            var result = EDGE_TO_SIZE_KEY.get(SORTED_SIZE_KEYS[SORTED_SIZE_KEYS.size - 1])
            for (edge in SORTED_SIZE_KEYS) {
                if (largestEdge <= edge) {
                    result = EDGE_TO_SIZE_KEY.get(edge)
                    break
                }
            }
            return result
        }

        private fun getPhotoUrl(photo: FlickrPhoto, width: Int, height: Int): String {
            Timber.d("getPhotoUrl = ${getPhotoUrlForSize(photo, getSizeKey(width, height))}")
            return getPhotoUrlForSize(photo, getSizeKey(width, height))
        }

        private fun getAlternateUrls(photo: FlickrPhoto, width: Int, height: Int): List<String> {
            val result = ArrayList<String>()
            for (sizeKey in getLargerSizeKeys(width, height)) {
                Timber.d("getAlternateUrls = ${getPhotoUrlForSize(photo, sizeKey)}")
                result.add(getPhotoUrlForSize(photo, sizeKey))
            }
            return result
        }

        private fun getPhotoUrlForSize(photo: FlickrPhoto, sizeKey: String): String {
            val entry = UrlCacheKey(photo, sizeKey)
            var result = CACHED_URLS.get(entry)
            if (result == null) {
                result = getPartialUrl(photo) + sizeKey + ".jpg"
                CACHED_URLS.put(entry, result)
            }
            return result
        }

        private fun getPartialUrl(photo: FlickrPhoto): String {
            return String.format(
                CACHEABLE_PHOTO_URL, photo.farm, photo.server, photo.id,
                photo.secret
            )
        }

        private fun getLargerSizeKeys(width: Int, height: Int): List<String> {
            val largestEdge = Math.max(width, height)

            var isFirstLargest = true
            val result = ArrayList<String>()
            val size = result.size
            for (i in 0 until size) {
                val edge = SORTED_SIZE_KEYS[i]
                if (largestEdge <= edge) {
                    if (isFirstLargest) {
                        isFirstLargest = false
                    } else {
                        result.add(EDGE_TO_SIZE_KEY.get(edge))
                    }
                }
            }
            return result

        }

        private class UrlCacheKey constructor(private val photo: FlickrPhoto, private val sizeKey: String) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UrlCacheKey

                if (photo != other.photo) return false
                if (sizeKey != other.sizeKey) return false

                return true
            }

            override fun hashCode(): Int {
                var result = photo.hashCode()
                result = 31 * result + sizeKey.hashCode()
                return result
            }
        }
    }
}
