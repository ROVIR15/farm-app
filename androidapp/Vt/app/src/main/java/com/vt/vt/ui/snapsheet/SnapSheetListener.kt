package com.vt.vt.ui.snapsheet

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface SnapSheetListener {
    fun getFile(file: File?)
    fun bitmapPhotos(photo: Bitmap?)
    fun uriFile(photo: Uri?)
}