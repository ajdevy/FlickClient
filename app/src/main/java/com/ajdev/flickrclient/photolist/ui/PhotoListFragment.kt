package com.ajdev.flickrclient.photolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ajdev.flickrclient.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_photo_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        loadPhotos()
    }

    private fun loadPhotos() {
        viewModel.recentPhotos
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            // will be disposed onDestroyView
            .autoDisposable(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    Timber.d("got recent photos $it")
                    // TODO: apply pagedList to photosAdapter
                    photosAdapter.submitList(it)
                },
                Timber::e
            )
    }

    private fun setupViews() {
        photosAdapter.itemClickedListener = { flickrPhoto ->
            val openPhotoDetailsAction = PhotoListFragmentDirections.actionPhotoListFragmentToPhotoDetailsFragment(
                flickrPhoto.largePhotoUrl,
                flickrPhoto.title
            )
            view?.findNavController()?.navigate(openPhotoDetailsAction)
        }

        photosRecycler.adapter = photosAdapter

        photosRecycler.layoutManager = GridLayoutManager(context, 2)
    }
}
