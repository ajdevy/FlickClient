package com.ajdev.flickrclient.photolist.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ajdev.flickrclient.app.media.images.GlideRequest
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.util.ViewPreloadSizeProvider
import java.util.*


class PhotosAdapter :
    PagedListAdapter<FlickrPhoto, PhotosAdapter.PhotoViewHolder>(diffUtilItemCallback),
    ListPreloader.PreloadModelProvider<FlickrPhoto> {

    var itemClickedListener: ((FlickrPhoto) -> Unit)? = null

    var fullPhotoRequest: GlideRequest<Drawable>? = null
    var thumbnailPhotoRequest: GlideRequest<Drawable>? = null

    private val preloadSizeProvider = ViewPreloadSizeProvider<FlickrPhoto>()

    override fun getPreloadItems(position: Int): MutableList<FlickrPhoto> {
        val item = getItem(position)
        return if (item == null) {
            Collections.emptyList()
        } else {
            Collections.singletonList(item)
        }
    }

    override fun getPreloadRequestBuilder(item: FlickrPhoto): RequestBuilder<*>? =
        fullPhotoRequest?.load(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.ajdev.flickrclient.R.layout.item_photo, parent, false)

        val holder = PhotoViewHolder(
            view = view,
            itemClickedListener = itemClickedListener,
            fullRequest = fullPhotoRequest,
            thumbRequest = thumbnailPhotoRequest
        )

        // set the view for the photo preload size
        preloadSizeProvider.setView(holder.photoDetailsImageView)

        return holder
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class PhotoViewHolder(
        private val view: View,
        private val itemClickedListener: ((FlickrPhoto) -> Unit)?,
        private val fullRequest: GlideRequest<Drawable>?,
        private val thumbRequest: GlideRequest<Drawable>?
    ) : RecyclerView.ViewHolder(view) {

        private val photoNameTextView: TextView = view.findViewById(com.ajdev.flickrclient.R.id.photoNameTextView)
        val photoDetailsImageView: ImageView = view.findViewById(com.ajdev.flickrclient.R.id.photoImageView)

        fun bind(item: FlickrPhoto) {

            if (item.title.isEmpty()) {
                photoNameTextView.visibility = View.INVISIBLE
            } else {
                photoNameTextView.visibility = View.VISIBLE
                photoNameTextView.text = item.title
            }

            view.setOnClickListener { itemClickedListener?.invoke(item) }

            fullRequest?.load(item)
                ?.thumbnail(thumbRequest?.load(item))
                ?.into(photoDetailsImageView)
        }
    }

    companion object {

        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<FlickrPhoto>() {
            override fun areItemsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FlickrPhoto, newItem: FlickrPhoto): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}