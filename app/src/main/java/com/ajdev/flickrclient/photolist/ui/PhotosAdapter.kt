package com.ajdev.flickrclient.photolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ajdev.flickrclient.R
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto

class PhotosAdapter : PagedListAdapter<FlickrPhoto, PhotosAdapter.PhotoViewHolder>(diffUtilItemCallback) {

    var itemClickedListener: ((FlickrPhoto) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)

        return PhotoViewHolder(view, itemClickedListener)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class PhotoViewHolder(
        private val view: View,
        private val itemClickedListener: ((FlickrPhoto) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {

        private val photoNameTextView = view.findViewById<TextView>(R.id.photoNameTextView)

        fun bind(item: FlickrPhoto) {

            photoNameTextView.text = item.title

            view.setOnClickListener { itemClickedListener?.invoke(item) }
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