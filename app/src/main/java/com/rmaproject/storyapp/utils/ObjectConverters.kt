package com.rmaproject.storyapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object ObjectConverters {

    fun isoDateFormatter(
        currentDate: String,
        targetTimeZone: String
    ) : String {
        val instant = Instant.parse(currentDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))

        return formatter.format(instant)
    }

    fun convertUriToBitmap(
        context: Context,
        uri: Uri
    ): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver,
                    uri
                ))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    fun latLongToActualLocation(context: Context, location: LatLng) : String {
        val latitude = location.latitude
        val longitude = location.longitude

        val geoCoder = Geocoder(context, Locale.getDefault()).getFromLocation(latitude, longitude, 1)?.first()

        return "${geoCoder?.subAdminArea},${geoCoder?.countryName}"
    }
}