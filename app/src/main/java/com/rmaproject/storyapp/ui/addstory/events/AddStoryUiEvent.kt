package com.rmaproject.storyapp.ui.addstory.events

import com.rmaproject.storyapp.data.remote.model.upload.UploadResponse

sealed class AddStoryUiEvent {
    object Loading : AddStoryUiEvent()
    data class Success(val response: UploadResponse) : AddStoryUiEvent()
    data class Error(val message: String) : AddStoryUiEvent()
    object EmptyDescription : AddStoryUiEvent()
    object EmptyImage : AddStoryUiEvent()
}