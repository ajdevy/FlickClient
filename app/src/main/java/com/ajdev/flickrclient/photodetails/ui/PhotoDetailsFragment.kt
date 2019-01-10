package com.ajdev.flickrclient.photodetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ajdev.flickrclient.R
import kotlinx.android.synthetic.main.fragment_photo_details.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory3

class PhotoDetailsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val photoTitleArgument by lazy {
        arguments?.let { PhotoDetailsFragmentArgs.fromBundle(it).stringPhotoTitle } ?: ""
    }

    private val photoUrlArgument by lazy {
        arguments?.let { PhotoDetailsFragmentArgs.fromBundle(it).stringPhotoUrl } ?: ""
    }

    private val viewModelFactory: (Fragment, String, String) -> PhotoDetailsViewModel by factory3()
    private val viewModel by lazy { viewModelFactory(this, photoTitleArgument, photoUrlArgument) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_photo_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoDetailsTitleTextView.text = viewModel.photoTitle
    }
}
