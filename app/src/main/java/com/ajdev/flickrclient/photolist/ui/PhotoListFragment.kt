package com.ajdev.flickrclient.photolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ajdev.flickrclient.R
import com.ajdev.flickrclient.flickr.data.FlickrGateway
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.photo_list_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import timber.log.Timber

class PhotoListFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: (Fragment) -> PhotoListViewModel by factory()
    private val viewModel by lazy { viewModelFactory(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.photo_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        message.setOnClickListener {
            it.findNavController().navigate(R.id.action_photoListFragment_to_photoDetailsFragment)
        }

        // FIXME: maybe the subscription should be in the view model? and expose only liveData,
        // FIXME: so that we have cached data?
        // FIXME: also need to autodispose or bindToLifecycle
        viewModel.getRecentPhotos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Timber.d("got recent photos $it")
                    // TODO: apply pagedList to adapter
                },
                Timber::e
            )
    }
}
