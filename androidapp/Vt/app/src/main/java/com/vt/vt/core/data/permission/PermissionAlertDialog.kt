package com.vt.vt.core.data.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

object PermissionAlertDialog {
    fun showPermissionDeniedDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Izinkan Kamera dan Gallery")
            .setMessage("Aktifkan izin kamera dan gallery di pengaturan aplikasi.")
            .setPositiveButton("OK") { dialog, which ->
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