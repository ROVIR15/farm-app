package com.vt.vt.core.data.permission

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {

    fun hasPermission() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val REQUIRED_PERMISSION = mutableListOf(
            Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(READ_MEDIA_IMAGES)
            } else {
                add(READ_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}