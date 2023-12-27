package com.vt.vt.ui.common

import android.graphics.Bitmap
import android.net.Uri
import okhttp3.MultipartBody
import java.io.File

interface SnapSheetListener {
    fun getFile(file: File?)
    fun bitmapPhotos(photo: Bitmap?)
    fun uriFile(photo: Uri?)
}