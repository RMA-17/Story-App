package com.rmaproject.storyapp.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rmaproject.storyapp.data.preferences.UserInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

fun getToken(): String {
    return "Bearer " + UserInfo.token
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun showSnackbar(
    rootView: View,
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT
) : Snackbar {
    return Snackbar.make(
        rootView, message, duration
    )
}

fun String.setRequestBody(): RequestBody {
    return this.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun File.buildImageBodyPart(): MultipartBody.Part {
    val imageReq = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        "photo",
        this.name,
        imageReq
    )
}

fun Bitmap.convertToFile(context: Context, fileName: String) : File {
    val file = File(context.cacheDir, fileName)
    file.createNewFile()
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 75, bos)
    val bitMapData = bos.toByteArray()
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

fun checkPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
