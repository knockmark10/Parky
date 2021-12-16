package com.markoid.parky.home.domain.usecases

import android.content.Context
import android.net.Uri
import com.markoid.files.enums.MediaType
import com.markoid.files.manager.AppFileManager
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.permissions.domain.exceptions.PermissionNotGrantedException
import com.markoid.parky.permissions.presentation.controllers.CameraPermissionController
import com.markoid.permissions.enums.AppPermissions
import com.markoid.permissions.helpers.takePicture
import com.markoid.permissions.managers.abstractions.ActivityResultManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TakeCarPictureUseCase
@Inject constructor(
    private val cameraPermissionController: CameraPermissionController,
    @ApplicationContext private val context: Context
) : UseCase<Uri, Unit>() {

    override suspend fun onExecute(request: Unit): Uri {
        if (cameraPermissionController.onRequestPermission(Unit).not())
            throw PermissionNotGrantedException(AppPermissions.Camera)
        val mediaUri = AppFileManager.getOutputMediaFile(context, MediaType.Image)
            ?: throw IllegalStateException("Invalid uri provided.")
        if (ActivityResultManager.getInstance().takePicture(mediaUri)) return mediaUri
        else throw IllegalStateException("Action was cancelled.")
    }
}
