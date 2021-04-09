package com.example.photoweatherapp.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

fun Uri.getFilePathFromURI(context: Context): String? {
    var filePath: String? = null
    if (DocumentsContract.isDocumentUri(context.applicationContext, this)) {
        val docId = DocumentsContract.getDocumentId(this)
        if ("com.android.providers.media.documents" == this.authority) {
            val id = docId.split(":")[1]
            val selection = MediaStore.Images.Media._ID + "=" + id
            filePath =
                getPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection,context)
        } else if ("com.android.providers.downloads.documents" == this.authority) {
            val contentUri = ContentUris.withAppendedId(
                Uri.parse(
                    "content://downloads/public_downloads"
                ), java.lang.Long.valueOf(docId)
            )
            filePath =
                getPath(contentUri, null,context)
        }
    } else if ("content".equals(this.scheme, ignoreCase = true)) {
        filePath =
            getPath(this ?: Uri.EMPTY, null,context)
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