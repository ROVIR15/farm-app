package com.vt.vt.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

object PermissionUtils {

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun showPermissionRationaleDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Izinkan Kamera")
            .setMessage("Perizinan kamera perlu diaktifkan.")
            .setPositiveButton("OK") { _, _ ->
                openAppSettings(context)
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }
}