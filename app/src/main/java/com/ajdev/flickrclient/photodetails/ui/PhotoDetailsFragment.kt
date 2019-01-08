package com.ajdev.flickrclient.photodetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ajdev.flickrclient.R
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class PhotoDetailsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModelFactory: (Fragment) -> PhotoDetailsViewModel by factory()
    private val viewModel by lazy { viewModelFactory(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.photo_details_fragment, container, false)

}
