package com.rmaproject.storyapp.data.remote.model.upload


import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)