package com.rmaproject.storyapp.ui.addstory.events

import android.content.Context
import android.graphics.Bitmap
import android.location.Location

sealed class AddStoryEvent {
    data class AddImage(val image: Bitmap) : AddStoryEvent()
    data class AddLocation(val location: Location?) : AddStoryEvent()
    data class AddDescription(val description: String) : AddStoryEvent()
    data class UploadStory(val context: Context) : AddStoryEvent()
}