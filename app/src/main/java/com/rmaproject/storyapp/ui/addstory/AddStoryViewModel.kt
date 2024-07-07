package com.rmaproject.storyapp.ui.addstory

import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmaproject.storyapp.data.repository.StoryRepository
import com.rmaproject.storyapp.ui.addstory.events.AddStoryEvent
import com.rmaproject.storyapp.ui.addstory.events.AddStoryUiEvent
import com.rmaproject.storyapp.utils.buildImageBodyPart
import com.rmaproject.storyapp.utils.convertToFile
import com.rmaproject.storyapp.utils.setRequestBody
import kotlinx.coroutines.launch

class AddStoryViewModel(
    private val repository: StoryRepository
) : ViewModel() {

    private val _imageToBeUploaded = MutableLiveData<Bitmap?>()
    val imageToBeUploaded: LiveData<Bitmap?> = _imageToBeUploaded

    private val _storyDescription = MutableLiveData<String>()

    private val _currentLocation = MutableLiveData<Location?>(null)

    private val _eventFlow = MutableLiveData<AddStoryUiEvent>()
    val eventFlow: LiveData<AddStoryUiEvent> = _eventFlow

    fun onEvent(event: AddStoryEvent) {
        when (event) {
            is AddStoryEvent.AddDescription -> {
                _storyDescription.value = event.description
            }
            is AddStoryEvent.AddImage -> {
                _imageToBeUploaded.value = event.image
            }
            is AddStoryEvent.UploadStory -> {
                viewModelScope.launch {
                    val description = _storyDescription.value
                    val image = _imageToBeUploaded.value
                    if (description == null) {
                        _eventFlow.postValue(AddStoryUiEvent.EmptyDescription)
                        return@launch
                    }
                    if (image == null) {
                        _eventFlow.postValue(AddStoryUiEvent.EmptyImage)
                        return@launch
                    }
                    val file = image.convertToFile(event.context, description)
                    try {
                        _eventFlow.postValue(AddStoryUiEvent.Loading)
                        val response =
                            if (_currentLocation.value == null) {
                                repository.uploadStories(
                                    description.setRequestBody(),
                                    file.buildImageBodyPart(),
                                )
                            } else {
                                repository.uploadStories(
                                    description.setRequestBody(),
                                    file.buildImageBodyPart(),
                                    _currentLocation.value
                                )
                            }
                        if (response.error) {
                            _eventFlow.postValue(AddStoryUiEvent.Error(response.message))
                            return@launch
                        }
                        _eventFlow.postValue(AddStoryUiEvent.Success(response))
                    } catch (e: Exception) {
                        Log.d("ERR_UPLOAD_STORY", e.toString())
                        _eventFlow.postValue(
                            AddStoryUiEvent.Error(
                                e.message ?: "Error when uploading story"
                            )
                        )
                    }
                }
            }
            is AddStoryEvent.AddLocation -> {
                _currentLocation.postValue(event.location)
            }
        }
    }
}