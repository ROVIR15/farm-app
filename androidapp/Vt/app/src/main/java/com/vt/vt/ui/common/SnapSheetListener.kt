package com.vt.vt.ui.common

import android.graphics.Bitmap
import android.net.Uri

interface SnapSheetListener {
    fun bitmapPhotos(photo: Bitmap?)
    fun uriFile(photo: Uri?)
}