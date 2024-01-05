package com.vt.vt.core.data.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.vt.vt.R

object PermissionAlertDialog {
    fun showPermissionDeniedDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(R.string.camera_and_gallery_permission)
            .setMessage(R.string.message_camera_and_gallery_permission)
            .setPositiveButton(R.string.yes) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setCancelable(true)
            .create()

        alertDialog.show()
    }
}