package com.vt.vt.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.vt.vt.R

object PermissionUtils {

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun showPermissionRationaleDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(Resources.getSystem().getString(R.string.camera_permission))
            .setMessage(Resources.getSystem().getString(R.string.message_camera_permission))
            .setPositiveButton(Resources.getSystem().getString(R.string.yes)) { _, _ ->
                openAppSettings(context)
            }
            .setNegativeButton(Resources.getSystem().getString(R.string.no), null)
            .create()

        alertDialog.show()
    }
}