package com.example.photoweatherapp.utils

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.photoweatherapp.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun Uri.getFilePathFromURI(context: Context): String? {
    var filePath: String? = null
    if (DocumentsContract.isDocumentUri(context.applicationContext, this)) {
        val docId = DocumentsContract.getDocumentId(this)
        if ("com.android.providers.media.documents" == this.authority) {
            val id = docId.split(":")[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            filePath =
                getPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context)
        } else if ("com.android.providers.downloads.documents" == this.authority) {
            val contentUri = ContentUris.withAppendedId(
                Uri.parse(
                    "content://downloads/public_downloads"
                ), java.lang.Long.valueOf(docId)
            )
            filePath =
                getPath(contentUri, null, context)
        }
    } else if ("content".equals(this.scheme, ignoreCase = true)) {
        filePath =
            getPath(this ?: Uri.EMPTY, null, context)
    } else if ("file".equals(this.scheme, ignoreCase = true)) {
        filePath = this.path
    }
    return filePath
}

private fun getPath(uri: Uri, selection: String?, context: Context): String {
    var path: String? = null
    val cursor = context.applicationContext
        ?.contentResolver
        ?.query(uri, null, selection, null, null)
    when {
        cursor != null -> {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
    }

    return path ?: ""
}

fun getUriFromFile(context: Context, file: File) =
    FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
        file
    )


fun shareImage(file: File, context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(
            Intent.EXTRA_STREAM, getUriFromFile(context, file)
        )
        .setType("image/png")
    context.startActivity(Intent.createChooser(shareIntent, "Share image"))
}

fun saveImageToStorage(context: Context, imageBitmap: Bitmap, block: (file: File) -> Unit) {
    var fOut: FileOutputStream? = null
    try {
        val file = createImageFile(context, FILE_SAVE_DIRECTORY)
        fOut = FileOutputStream(file)
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        block.invoke(file)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fOut?.flush()
            fOut?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun createImageFile(
    context: Context,
    directoryPath: String = Environment.DIRECTORY_PICTURES
): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = context.getExternalFilesDir(directoryPath)!!
    storageDir.mkdir()
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}