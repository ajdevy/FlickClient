package com.ajdev.flickrclient.photolist.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ajdev.flickrclient.R
import com.ajdev.flickrclient.app.media.images.GlideApp
import com.ajdev.flickrclient.app.media.images.GlideRequest
import com.ajdev.flickrclient.flickr.data.model.FlickrPhoto
import com.ajdev.flickrclient.photolist.ui.preload.FlickrModelLoader
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_photo_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import timber.log.Timber


class PhotoListFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: (Fragment) -> PhotoListViewModel by factory()
    private val viewModel by lazy { viewModelFactory(this) }

    private var photosAdapter = PhotosAdapter()
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_photo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        loadPhotos()

        if (savedInstanceState != null) {
            val index = savedInstanceState.getInt(STATE_POSITION_INDEX)
            val offset = savedInstanceState.getInt(STATE_POSITION_OFFSET)
            layoutManager.scrollToPositionWithOffset(index, offset)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (photosRecycler != null) {
            val index = layoutManager.findFirstVisibleItemPosition()
            val topView = photosRecycler.getChildAt(0)
            val offset = topView?.top ?: 0
            outState.putInt(STATE_POSITION_INDEX, index)
            outState.putInt(STATE_POSITION_OFFSET, offset)
        }
    }

    private fun loadPhotos() {
        viewModel.recentPhotos
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            // will be disposed onDestroyView
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    Timber.d("got recent photos ${it.size}")
                    photosAdapter.submitList(it)
                },
                Timber::e
            )
    }

    private fun setupViews() {
        setupPhotoPreload()

        photosAdapter.itemClickedListener = { flickrPhoto ->
            val openPhotoDetailsAction =
                PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDetailsFragment(flickrPhoto)
            view?.findNavController()?.navigate(openPhotoDetailsAction)
        }

        photosRecycler.adapter = photosAdapter

        layoutManager = GridLayoutManager(context, 2)

        photosRecycler.layoutManager = layoutManager
    }

    private fun setupPhotoPreload() {

        val preloadSizeProvider = ViewPreloadSizeProvider<FlickrPhoto>()

        val preloader = RecyclerViewPreloader<FlickrPhoto>(
            GlideApp.with(this), photosAdapter, preloadSizeProvider, PRELOAD_ITEMS_AHEAD_COUNT
        )
        photosRecycler.addOnScrollListener(preloader)
        photosRecycler.setItemViewCacheSize(0)

        val glideRequests = GlideApp.with(this)

        val fullPhotoRequest: GlideRequest<Drawable> = glideRequests
            .asDrawable()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(ColorDrawable(Color.GRAY))

        val thumbnailPhotoRequest: GlideRequest<Drawable> = glideRequests
            .asDrawable()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(FlickrModelLoader.SQUARE_THUMB_SIZE_KEY)
            .transition(DrawableTransitionOptions.withCrossFade())

        // needs to be assigned later, since the preloader cannot be created without creating the adapter first
        photosAdapter.fullPhotoRequest = fullPhotoRequest
        photosAdapter.thumbnailPhotoRequest = thumbnailPhotoRequest

        photosRecycler.setRecyclerListener { holder ->
            val photoViewHolder = holder as PhotosAdapter.PhotoViewHolder
            glideRequests.clear(photoViewHolder.photoDetailsImageView)
        }
    }

    companion object {
        private const val PRELOAD_ITEMS_AHEAD_COUNT = 10
        private const val STATE_POSITION_INDEX = "state_position_index"
        private const val STATE_POSITION_OFFSET = "state_position_offset"
    }
}

