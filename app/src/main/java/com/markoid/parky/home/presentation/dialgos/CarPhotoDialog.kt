package com.markoid.parky.home.presentation.dialgos

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.databinding.DialogCarPhotoBinding

class CarPhotoDialog : AbstractDialog<DialogCarPhotoBinding>() {

    override fun getStyle(): Int = R.style.FullWidthDialog

    private val photoUri: Uri?
        get() = arguments?.getString(PHOTO_URI)?.let { Uri.parse(it) }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogCarPhotoBinding = DialogCarPhotoBinding
        .inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.carPhoto.setImageURI(photoUri)
        binding.actionAccept.setOnClickListener { dismiss() }
    }

    companion object {
        private const val PHOTO_URI = "photo.uri"
        fun newInstance(photoUri: Uri?): CarPhotoDialog = CarPhotoDialog().apply {
            arguments = Bundle().apply { putString(PHOTO_URI, photoUri?.toString().orEmpty()) }
        }
    }
}
