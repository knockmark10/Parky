package com.markoid.files.manager

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.markoid.files.R
import com.markoid.files.enums.MediaType
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File

object AppFileManager {

    fun getOutputMediaFile(context: Context, type: MediaType): Uri? {
        val fileName = getOutputMediaFileName(context, type) ?: return null
        return generateUri(context, File(fileName))
    }

    private fun getOutputMediaFileName(context: Context, type: MediaType): String? {
        val mediaStorageDir =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Parky")
        if (mediaStorageDir.exists().not()) {
            if (mediaStorageDir.mkdirs().not()) return null
        }

        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd-HH.mm.ss")
        val timestamp = formatter.print(DateTime.now())

        return when (type) {
            MediaType.Image -> mediaStorageDir.path + File.separator + "IMG_" + timestamp + ".jpg"
            MediaType.Video -> mediaStorageDir.path + File.separator + "VID_" + timestamp + ".mp4"
        }
    }

    private fun generateUri(context: Context, file: File): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.getString(R.string.file_provider_authority),
                file
            )
        } else {
            Uri.fromFile(file)
        }
}
