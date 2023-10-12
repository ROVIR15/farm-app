package com.vt.vt.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


/*fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}*/
fun Spinner.selected(action: (position: Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(
            parent: AdapterView<*>?, view: View?, position: Int, id: Long
        ) {
            action(position)
        }
    }
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1  // Months are 0-based
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return "$year-$month-$day"
}
fun formatterDateFromCalendar(inputDate: String): String {
    val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return try {
        val date = inputDateFormat.parse(inputDate)
        outputDateFormat.format(date!!)
    } catch (e: Exception) {
        // Handle parsing or formatting exceptions
        inputDate // Return the original input if an exception occurs
    }
}

fun formatDate(dateTime: String?, dateFormats: String?): String {
    val sdf =
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            Locale.getDefault()
        )

    val date = sdf.parse(dateTime!!)
    val calendar = Calendar.getInstance()
    calendar.time = date!!

    val output =
        SimpleDateFormat(
            dateFormats,
            Locale("in", "ID")
        )

    return output.format(calendar.time)
}

fun formatDateBreeding(dateTime: String?, dateFormats: String?): String {
    val sdf =
        SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSSSSS",
            Locale.getDefault()
        )

    val date = sdf.parse(dateTime!!)
    val calendar = Calendar.getInstance()
    calendar.time = date!!

    val output =
        SimpleDateFormat(
            dateFormats,
            Locale("in", "ID")
        )

    return output.format(calendar.time)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())


@Throws(IOException::class)
fun getRotateImage(photoPath: String, bitmap: Bitmap?): Bitmap? {
    val ei = ExifInterface(photoPath)
    val orientation: Int = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    var rotatedBitmap: Bitmap? = null
    rotatedBitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(
            bitmap!!, 90
        )

        ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(
            bitmap!!, 180
        )

        ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(
            bitmap!!, 270
        )

        ExifInterface.ORIENTATION_NORMAL -> bitmap
        else -> bitmap
    }
    return rotatedBitmap
}
