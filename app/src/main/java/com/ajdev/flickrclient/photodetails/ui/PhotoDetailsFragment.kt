package com.ajdev.flickrclient.photodetails.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ajdev.flickrclient.R
import com.ajdev.flickrclient.app.media.images.GlideApp
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_photo_details.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory2

class PhotoDetailsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val flickrPhotoArgument by lazy {
        arguments?.let { PhotoDetailsFragmentArgs.fromBundle(it).flickrPhoto }
            ?: throw IllegalArgumentException("Could not retrieve flickrPhoto Argument")
    }

    private val viewModelFactory: (Fragment, FlickrPhoto) -> PhotoDetailsViewModel by factory2()
    private val viewModel by lazy { viewModelFactory(this, flickrPhotoArgument) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_photo_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.flickrPhoto.title.isEmpty()) {
            photoDetailsTitleTextView.visibility = View.INVISIBLE
        } else {
            photoDetailsTitleTextView.text = viewModel.flickrPhoto.title

        }

        GlideApp.with(this)
            .asDrawable()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            // FIXME: add thumbnail loading as placeholder first?
            .placeholder(ColorDrawable(Color.GRAY))
            .load(viewModel.photoUrl)
            .into(photoDetailsImageView)
    }
}
