package com.markoid.parky.home.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.databinding.FragmentGalleryBinding
import com.markoid.parky.home.presentation.viewmodels.GalleryViewModel

class GalleryFragment : AbstractFragment<FragmentGalleryBinding>() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGalleryBinding = FragmentGalleryBinding.inflate(inflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner, { textView.text = it })
    }
}
